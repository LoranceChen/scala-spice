package frp.imonix

import monix.execution.Scheduler.{global => scheduler}
import monix.reactive.subjects.{PublishSubject, Subject}

class SchedulerDelayTest {
  import scala.concurrent.duration._

  val c = scheduler.scheduleOnce(5.seconds) {
    println("Hello, world!")
  }

  val sub = PublishSubject[Int]()

  sub.map(x => x).timeoutOnSlowUpstream(30 seconds)
  Thread.currentThread().join()
}
