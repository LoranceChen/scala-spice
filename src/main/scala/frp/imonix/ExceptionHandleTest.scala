package frp.imonix

import java.util.concurrent.Executors

import monix.eval.Task
import monix.execution.ExecutionModel.AlwaysAsyncExecution
import monix.execution.UncaughtExceptionReporter
import monix.execution.schedulers.ExecutorScheduler
import monix.reactive.Observable

import scala.concurrent.Future


object ExceptionHandleTest extends App {

  implicit val subscriberScheduler = ExecutorScheduler(
    Executors.newFixedThreadPool(4),
    UncaughtExceptionReporter(t => println(s"this should not happen: ${t.getMessage}")),
    AlwaysAsyncExecution
  )

  val stream = Observable.fromAsyncStateAction[Int, Int](_ => Task.fromFuture(
    Future.failed(new RuntimeException("Bang!"))
  ))(0)
    .onErrorHandleWith {
      case t =>
        println(s"we should see this, shouldn't we? - $t")
        Observable.fromAsyncStateAction[Int, Int](prev => Task.fromFuture(
          Future.successful(prev + 1, prev + 1))
        )(0)
    }.take(100)

  stream.foreach(println)
}