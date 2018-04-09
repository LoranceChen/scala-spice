package testmonix

import monix.execution.Ack.{Continue, Stop}
import monix.reactive.subjects.PublishSubject
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable
import monix.reactive.OverflowStrategy.BackPressure
import monix.reactive.observers.{BufferedSubscriber, Subscriber}

/**
  *
  */
object SimpleUsage extends App {
  def log(msg: Any) = println(s"${Thread.currentThread().getName} - $msg")
  val obv = PublishSubject[Long]

  val x = obv.publish

  val a = obv.map(x => {x + 1})
  val f1: Observable[Long] = a.map(x => {println(s"a map - $x"); x})

  f1.subscribe()
  obv.subscribe{x => log(s"ask - $x"); Continue}

  val f2 = f1.zip(obv).map(x => {
    println(s"zip - ${x._1 * x._2}")
    x
  })
  f2.subscribe()

  f1.filter(x => x % 2 == 0).map(x => {println(s"filter - $x");x}).subscribe()


  obv.onNext(10L)
  obv.onNext(11L)

  x.connect()

  Thread.currentThread().join()
}


/**
  *
  */
object BackpresureTest extends App {
  def log(msg: Any) = println(s"${Thread.currentThread().getName} - $msg")
  val obv = PublishSubject[Long]
  obv.onNext(10L)

  val a = obv.map(x => {log(s"x - $x");x})
  a.subscribe(BufferedSubscriber(Subscriber.empty, BackPressure(100)))

  obv.onNext(11L).foreach(x => log(s"ask - $x"))


  Thread.currentThread().join()
}


/**
  *
  */
object SubscribeTest extends App {
  def log(msg: Any) = println(s"${Thread.currentThread().getName} - $msg")

  import scala.concurrent.duration._

  val obv = Observable.interval(1 second).take(10)
  val obv2 = obv.map{x => println(s"map1 - ${x}"); x}//.share

  obv2.subscribe(x => {
    println("stopable - " + x)
    if(x == 5) Stop
    else Continue
  },
    e => println("e - " + e),
    () => println("completed - ")
  )

  obv2.subscribe( { x =>
    println("xxxx - " + x)
    Continue
  },
    e => println("eeee - " + e),
    () => println("completedeeeee - ")
  )

  Thread.currentThread().join()
}
