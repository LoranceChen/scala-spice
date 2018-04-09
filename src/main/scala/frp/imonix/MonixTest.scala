package frp.imonix

import scala.concurrent.duration._
import monix.execution.Scheduler.{global => scheduler}

/**
  *
  */
object MonixTest extends App {
  val c = scheduler.scheduleAtFixedRate(
    3 seconds, 5 seconds) {
      println("Fixed delay task")
    }

  Thread.sleep(7000)
  // If we change our mind and want to cancel
  c.cancel()

  println(scheduler.executionModel)
}
