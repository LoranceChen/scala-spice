package frp.imonix

import java.util.concurrent.Executors

import monix.eval.Task
import monix.execution.{Ack, Scheduler}
import monix.execution.Ack.{Continue, Stop}
import monix.execution.atomic.AtomicInt
import monix.reactive.subjects.PublishSubject
import monix.reactive.{Observable, OverflowStrategy}

import scala.concurrent.Future

/**
  * test backpressure and try limit meesage under a count
  */
object BackpressureWithWait extends App {

  val source = PublishSubject[Int]()
  val backSource = source.map(_ * 10).asyncBoundary(OverflowStrategy.BackPressure(4))
//  val backSource2 = source.map(_ * 10).asyncBoundary(OverflowStrategy.BackPressure(20))
//  val backSource = source.whileBusyBuffer(OverflowStrategy.BackPressure)


  lazy val scheduler = {
    val javaService = Executors.newScheduledThreadPool(1)
    Scheduler(javaService)
  }

  //nextFn: A => Future[Ack], errorFn: Throwable => Unit, completedFn: () => Unit
  def subSleep = backSource.subscribe(
    nextFn => {
      println(s"${Thread.currentThread().getId} - get $nextFn")
      Thread.sleep(500)
      Continue
    },
    errorFn => {
      println(s"${Thread.currentThread().getId} - get ${errorFn.getMessage}")
    },
    () => {
      println("completed")
    }
  )(scheduler)



  val sche = {
    val javaService = Executors.newScheduledThreadPool(2)
    Scheduler(javaService)
  }

  val sche2 = {
    val javaService = Executors.newScheduledThreadPool(10)
    Scheduler(javaService)
  }
  //nextFn: A => Future[Ack], errorFn: Throwable => Unit, completedFn: () => Unit
  def sub2 = source.subscribe(
    nextFn => {
      println(s"${Thread.currentThread().getId} - get222 $nextFn")
//      Thread.sleep(500)
//      Continue
      Stop
    },
    errorFn => {
      println(s"${Thread.currentThread().getId} - get222 ${errorFn.getMessage}")
    },
    () => {
      println("completed")
    }
  )(sche2)

  val x = AtomicInt(1)

  def sub3 = source.onErrorHandleWith{
    case x =>
      println(s"hahaha get exception - $x")
      Observable.fromAsyncStateAction[Int, Int](prev => Task.fromFuture(
        Future.successful(prev + 1, prev + 1))
      )(0)
  }.subscribe(
    nextFn => {
      println(s"${Thread.currentThread().getName} - get333 $nextFn")
      //      Thread.sleep(500)
      //      Continue
      val oldX = x.getAndAdd(1)
      if(oldX == 10) {
        throw new Exception("OMG!")
      }
      Continue
    },
    errorFn => {
      println(s"${Thread.currentThread().getName} - get222 error - ${errorFn.getMessage}")
    },
    () => {
      println("completed")
    }
  )(sche2)

  subSleep
//  sub2
//  sub3

  def doOnNext(int: Int): Future[Ack] = {
    val rst = source.onNext(int).map{
      case Continue =>
        val x: Ack = if(int < 100) {
          if(int == 5) {
            throw new Exception("error on do next")
          }
          doOnNext(int + 1)
          println(s"${Thread.currentThread().getId} - do next - $int")

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
