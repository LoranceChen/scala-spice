package framework.actorserver

import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext.Implicits.global

object TestMain extends App {
  implicit val akkaSys = ActorSystem("test")
  val server = new Server()
  val mod = new MyMod(server)

  val ref = server.start('MyMod, mod)

  val r1 = mod.add('joe, "at home")

  r1.foreach(println) // result: 'ok

  Thread.sleep(1000)

  val r2 = mod.whereid('joe2)

  r2.foreach(println) // result: Some("at home")

  Thread.currentThread().join()
}