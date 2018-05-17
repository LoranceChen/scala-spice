package frp.imonix

import monix.execution.Ack.Continue
import monix.reactive.Observable

import scala.concurrent.duration._
import monix.execution.Scheduler.Implicits.global

object MonixObservableExample extends App {

  val intSrc = Observable.interval(1.second)

  intSrc.subscribe(x => {
    println("s1 - " + x)
    Continue
  })

  Thread.sleep(1000 * 5)

  intSrc.subscribe(x => {
    println("s2 - " + x)
    Continue
  })

  Thread.currentThread().join()

}
