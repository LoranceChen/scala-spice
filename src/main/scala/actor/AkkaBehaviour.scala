package actor

import akka.actor.{Actor, ActorSystem, Props}


object AkkaBehaviour extends App {
  val actorRoot = ActorSystem("test")
  val a = actorRoot.actorOf(Props[AkkaBehaviour]())

  a ! 'do_1
  a ! 'do_2
  a ! 'do_3

  Thread.currentThread().join()
}

class AkkaBehaviour extends Actor {
  override def receive: Receive = {
    case 'do_1 => println("do_1")
    case 'do_2 => context.become({
      case 'do_3 => println("do_2 and then do_3")
    })
  }

}
