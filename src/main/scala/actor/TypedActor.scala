package actor

import akka.typed._
import akka.typed.scaladsl.Actor
import akka.typed.scaladsl.AskPattern._
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.Await

object HelloWorld {
  final case class Greet(whom: String, replyTo: ActorRef[Greeted])
  final case class Greeted(whom: String)

  val greeter = Actor.immutable[Greet] { (x, msg) ⇒
    println(s"Hello ${msg.whom}!")
    msg.replyTo ! Greeted(msg.whom)
    Actor.same
  }

}

object ChatRoom {

  sealed trait Command
  final case class GetSession(screenName: String, replyTo: ActorRef[SessionEvent])
    extends Command

  sealed trait SessionEvent
  final case class SessionGranted(handle: ActorRef[PostMessage]) extends SessionEvent
  final case class SessionDenied(reason: String) extends SessionEvent
  final case class MessagePosted(screenName: String, message: String) extends SessionEvent

  final case class PostMessage(message: String)

  private final case class PostSessionMessage(screenName: String, message: String)
    extends Command

  val behavior: Behavior[Command] =
    chatRoom(List.empty)

  private def chatRoom(sessions: List[ActorRef[SessionEvent]]): Behavior[Command] =
    Actor.immutable[Command] { (ctx, msg) ⇒
      msg match {
        case GetSession(screenName, client) ⇒
          // 由spawnAdapter生成的wrapper actor会把收到的PostMessage转化成PostSessionMessage，然后发送到ctx的这个当前的actor中
          val wrapper = ctx.spawnAdapter {
            p: PostMessage ⇒ PostSessionMessage(screenName, p.message)
          }
          client ! SessionGranted(wrapper)
          chatRoom(client :: sessions)
        case PostSessionMessage(screenName, message) ⇒
          val mp = MessagePosted(screenName, message)
          sessions foreach (_ ! mp)
          Actor.same
      }
    }
}

/**
  *
  */
object TypedActor extends App {
  def testHelloWorld = {
    import HelloWorld._
    // using global pool since we want to run tasks after system.terminate
    import scala.concurrent.ExecutionContext.Implicits.global

    val system: ActorSystem[Greet] = ActorSystem(greeter, "hello")

    implicit val timeout = Timeout(3.seconds)
    implicit val sd = system.scheduler

    val future: Future[Greeted] = system ?[Greeted] (Greet("world", _))


    for {
      greeting <- future.recover { case ex ⇒ ex.getMessage }
      done ← {
        println(s"result: $greeting"); system.terminate()
      }
    } println("system terminated")

  }

  def testChatRoom = {
    import ChatRoom._

    val gabbler =
      Actor.immutable[SessionEvent] { (_, msg) ⇒
        msg match {
          case SessionGranted(handle) ⇒
            handle ! PostMessage("Hello World!")
            Actor.same
          case MessagePosted(screenName, message) ⇒
            println(s"message has been posted by '$screenName': $message")
            Actor.stopped
        }
      }

    val main: Behavior[akka.NotUsed] =
      Actor.deferred { ctx ⇒
        val chatRoom = ctx.spawn(ChatRoom.behavior, "chatroom")
        val gabblerRef = ctx.spawn(gabbler, "gabbler")
        ctx.watch(gabblerRef)
        chatRoom ! GetSession("ol’ Gabbler", gabblerRef)

        Actor.immutable[akka.NotUsed] {
          (_, _) ⇒ Actor.unhandled
        } onSignal {
          case (ctx, Terminated(ref)) ⇒
            Actor.stopped
        }
      }

    val system = ActorSystem(main, "ChatRoomDemo")
    Await.result(system.whenTerminated, 3.seconds)
  }

//  testHelloWorld
  testChatRoom
}
