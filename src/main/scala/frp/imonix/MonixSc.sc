//// We need a scheduler whenever asynchronous
//// execution happens, substituting your ExecutionContext
//import monix.execution.{Ack, CancelableFuture}
//import monix.execution.Scheduler.Implicits.global
//import monix.reactive.{Observable, OverflowStrategy}
//import monix.reactive.observers.{BufferedSubscriber, CacheUntilConnectSubscriber, Subscriber}
//
//// Needed below
//import scala.concurrent.Await
//import scala.concurrent.duration._
//
//val source = Observable.interval(1.second)
//  // Filtering out odd numbers, making it emit every 2 seconds
//  .filter(_ % 2 == 0)
//  // We then make it emit the same element twice
//  .flatMap(x => Observable(x, x))
//  // This stream would be infinite, so we limit it to 10 items
//  .take(10)
//
//// Observables are lazy, nothing happens until you subscribe...
//val cancelable = source
//  // On consuming it, we want to dump the contents to stdout
//  // for debugging purposes
//  .dump("O")
//  // Finally, start consuming it
//  .subscribe()
//
//
//val underlying = Subscriber.dump("O")
//val subscriber = CacheUntilConnectSubscriber(underlying)
//
//// Gets cached in an underlying buffer
//// to be streamed after connect
//subscriber.onNext(10)
//// res0: Future[Ack] = Continue
//subscriber.onNext(20)
//// res1: Future[Ack] = Continue
//subscriber.onNext(30)
//// res2: Future[Ack] = Continue
//
//val result: CancelableFuture[Ack] =
//  subscriber.connect()
//
//val bufSub = BufferedSubscriber(underlying, OverflowStrategy.BackPressure(2))
//
//
//bufSub.onNext(1)
//
//bufSub.onNext(2)
//
//bufSub.onNext(3)


import scala.concurrent.duration._
import monix.execution.Scheduler.{global => scheduler}
//
//val c = scheduler.scheduleAtFixedRate(
//  3 seconds, 5 seconds) {
//  println("Fixed delay task")
//}
//
//// If we change our mind and want to cancel
//c.cancel()

scheduler.executionModel