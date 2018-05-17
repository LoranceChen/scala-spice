package frp.imonix

import java.util.concurrent.Executors

import monix.execution.{Ack, Scheduler}
import monix.execution.Ack.{Continue, Stop}
import monix.execution.atomic.AtomicInt
import monix.reactive.OverflowStrategy
import monix.reactive.subjects.PublishSubject

import scala.concurrent.ExecutionContext.Implicits.global
//import monix.execution.Scheduler.Implicits

import scala.concurrent.Future

/**
  * test Monix stream works at single thread.
  * 推论：
  * 1. 中间的每一个map、flatmap、subscribe操作都保证是有先后顺序的
  * 2. 除了默认状态，每一个调度器都有自己的BackPressure策略
  * 3. BackPressure策略设定的值是大致范围，不会精确到缓存这个数值
  *
  * 其他：
  * 1. 有趣的现象是print出来的pair值，如（1,1), (2, 3), (2, 10)等，这里收到很多2是因为
  * src源在count加之前就将count的值发送出去了。
  */
object MonixAckSeq extends App {

  lazy val schedulerSub = {
    val javaService = Executors.newScheduledThreadPool(2)
    Scheduler(javaService)
  }

  lazy val schedulerOnNext = {
    val javaService = Executors.newScheduledThreadPool(2)
    Scheduler(javaService)
  }

  lazy val schedulerObv = {
    val javaService = Executors.newScheduledThreadPool(2)
    Scheduler(javaService)
  }

  @volatile var count = 1

  val countAtom = AtomicInt(count)
  val countMax = 100000
  val src = PublishSubject[Int]

  src
    .asyncBoundary(OverflowStrategy.BackPressure(3))
//    .observeOn(schedulerObv, OverflowStrategy.BackPressure(2))
    .map{x =>
//      Thread.sleep(1000 * 1)
      val c = count
      count += 1
      (x, c)
    }
    .subscribe(
      x => {
        x match {
          case (a, b) =>
            //        val c = count
            //        count += 1
            //        c
            Thread.sleep(1000 * 1)
            log(s"count - $x")
            Continue
        }

      },
      err => {
        log("error: " + err)

      },
      () => {

        log("completed: " + assert(countMax == count))
      }
    )(schedulerSub)

  def onNextLoop(): Future[Ack] = {
    log(s"do next - ${countAtom.get}")
    src.onNext(count).map{
      case Continue =>
        if(countAtom.get == countMax) {
          Stop
        }
        else {
          countAtom.add(1)
          onNextLoop()
        }
        Continue
      case Stop =>
        Stop
    }(schedulerOnNext)

  }

  onNextLoop()


  def log(msg: Object) = {
    println(Thread.currentThread().getName + " - " + msg)
  }

  Thread.currentThread().join()
}
