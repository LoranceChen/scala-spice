package frp.imonix

import java.util.concurrent.Executors

import monix.execution.{Ack, Scheduler}
import monix.execution.Ack.{Continue, Stop}
import monix.reactive.subjects.{PublishSubject, ReplaySubject, Subject}

import scala.concurrent.Future

/**
  * test backpressure and try limit meesage under a count
  */
object SchedulerTest extends App {

  def log(msg: Object) = {
    println(s"${Thread.currentThread().getName}:${Thread.currentThread().getId} - $msg")
  }

  lazy val schedulerOneThread0 = {
    val javaService = Executors.newScheduledThreadPool(1)
    Scheduler(javaService)
  }

  lazy val schedulerOneThread1 = {
    val javaService = Executors.newScheduledThreadPool(1)
    Scheduler(javaService)
  }

  lazy val schedulerOneThread2 = {
    val javaService = Executors.newScheduledThreadPool(1)
    Scheduler(javaService)
  }

  lazy val schedulerOneThread3 = {
    val javaService = Executors.newScheduledThreadPool(1)
    Scheduler(javaService)
  }

  val source = ReplaySubject[Int]()

  source.doOnNext{x =>
    log("doOnNext - x")
  }

  val map = source
  .subscribeOn(schedulerOneThread0)
  .map{x =>
    log("map1")
    x
  }
  .map { x =>
    log("map2")
    x
  }
  .observeOn(schedulerOneThread1)
  .map{x =>
    log("map3")
    x
  }
  .map{x =>
    log("map4")
    x
  }
  .observeOn(schedulerOneThread2)
  .map{x =>
    log("map5")
    x
  }
  .map{x =>
    log("map6")
    x
  }
  .observeOn(schedulerOneThread3)
  .map{x =>
    log("map7")
    x
  }
  .subscribe(x => {
    log(s"sub - $x")
    Continue
  })(monix.execution.Scheduler.Implicits.global)
//
//  source.subscribe(x => {
//    log(s"sub - $x")
//    Continue
//  })(monix.execution.Scheduler.Implicits.global)


  Thread.sleep(1000 * 2)
  def doOnNext(int: Int): Future[Ack] = {

    val rst = source.onNext{
      log("on next")
      1
    }.map{
      case Continue =>
        val x: Ack = if(int < 1) {
          doOnNext(int + 1)
          log(s"do next - $int")

          Continue
        } else {
          Stop
        }
        x

      case Stop =>
        Stop
    }(monix.execution.Scheduler.Implicits.global)

    rst
  }

  doOnNext(1)

  Thread.currentThread().join()
}
