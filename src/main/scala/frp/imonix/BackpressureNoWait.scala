package frp.imonix

import java.util.concurrent.Executors

import monix.execution.Ack.{Continue, Stop}
import monix.execution.atomic.AtomicInt
import monix.execution.{Ack, Scheduler}
import monix.reactive.OverflowStrategy
import monix.reactive.subjects.PublishSubject

import scala.concurrent.Future

/**
  * test backpressure and try limit meesage under a count
  */
object BackpressureNoWait extends App {

  val source = PublishSubject[Int]()
  val backSource = source.asyncBoundary(OverflowStrategy.BackPressure(10))
  val backSource2 = backSource.map(x => x * 2)
//  val backSource = source.whileBusyBuffer(OverflowStrategy.Unbounded)


  lazy val scheduler = {
    val javaService = Executors.newScheduledThreadPool(1)
    Scheduler(javaService)
  }
  val x = AtomicInt(1)
  //nextFn: A => Future[Ack], errorFn: Throwable => Unit, completedFn: () => Unit
  val sub = backSource2.subscribe(
    nextFn => {
      println(s"${Thread.currentThread().getName} - get $nextFn")
      Thread.sleep(100)
      val oldX = x.getAndAdd(1)
      if(oldX == 10) {
        throw new Exception("OMG!")
      }
      Continue
    },
    errorFn => {
      println(s"${Thread.currentThread().getName} - get error ${errorFn.getMessage}")
    },
    () => {
      println("completed")
    }
  )(scheduler)

  def doOnNextNotWait(int: Int) = {
    (1 to int).toList.foreach{x =>
      println(s"${Thread.currentThread().getName} - next - $x")
      source.onNext(x)
    }
  }

  doOnNextNotWait(100)// 到100万也不会报错，记得rxjava会报错，抛出发送速度过快的异常
  Thread.currentThread().join()
}
