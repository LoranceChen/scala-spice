package framework.actorserver

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import scala.concurrent.{Future, Promise}

/**
  *
  */
class Server(implicit actorSys: ActorSystem) {

  def start(name: Symbol, mod: Mod): Unit = {
    Server.register(name, actorSys.actorOf(Props(new MyActor(name, mod, mod.init()))))
  }

  def rpc[Req, Rsp](name: Symbol, request: Req)(implicit mf: Manifest[Rsp]): Future[Rsp] = {
    class Handle(result: Promise[Rsp]) extends Actor {
      override def receive: Receive = {
        case (_: Symbol, response: Rsp) =>
          result.trySuccess(response)
          context.stop(self)
      }
    }

    val promise = Promise[Rsp]
    val handler = actorSys.actorOf(Props(new Handle(promise)))

    Server.getActor(name) ! (handler, request)

    promise.future
  }

  class MyActor(name: Symbol, mod: Mod, state: State) extends Actor {
    override def receive: Receive = loop(name: Symbol, mod: Mod, state: State)

    def loop(name: Symbol, mod: Mod, state: State): Receive = {
      case (sdr: ActorRef, request) =>
        val (response, state1) = mod.handle(request, state)
        sdr ! (name, response)
        context.become(loop(name: Symbol, mod, state1))
    }
  }

}

object Server {
  val map = collection.mutable.HashMap[Symbol, ActorRef]()

  def register(name: Symbol, actorRef: ActorRef) = {
    map.put(name, actorRef)

  }

  def getActor(name: Symbol) = map(name)


}
