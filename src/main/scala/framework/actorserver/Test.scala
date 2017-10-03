package framework.actorserver

import akka.actor.ActorSystem


object Test extends App {
  implicit val akkaSys = ActorSystem("test")
  val server = new Server()
  val mod = new MyMod(server)

  val ref = server.start('MyMod, mod)

  val r1 = mod.add('joe, "at home")

  println(r1)

  val r2 = mod.whereid('joe)

  println(r2)

}