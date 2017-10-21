package framework.actorserver

import akka.actor.{Actor, ActorSystem, Props}

/**
  *
  */
object ActorTimeSpeed extends App {
  val system = ActorSystem.create("aaa")
  def warmup(x: => Unit): Unit = {
    for(i <- 1 to 1000) {
      x
    }
  }

  def timeDebug(x: => Unit): Unit = {
    val begin = System.nanoTime()
    x
    println("cost - " + (System.nanoTime() - begin))
  }

  class ActorA extends Actor {
    override def receive: Receive = {
      case 1 => println(1)
    }
  }

  def newActor = {
    var i = 0
    while({i += 1; i < 40000}) {
      system.actorOf(Props[ActorA])
    }
  }

  timeDebug(newActor)//669164625
  timeDebug(newActor)//293,382,009 ns == 293ms;
  timeDebug(newActor)
  timeDebug(newActor)
  timeDebug(newActor)
}
