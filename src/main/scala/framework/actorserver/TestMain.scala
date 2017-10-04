package framework.actorserver

import akka.actor.ActorSystem


object TestMain extends App {
  implicit val akkaSys = ActorSystem("test")
  val server = new Server()
  val mod = new MyMod(server)

  val ref = server.start('MyMod, mod)

  val r1 = mod.add('joe, "at home")

  println(r1) // result: 'ok

  val r2 = mod.whereid('joe)

  r2.foreach(println) // result: Some("at home")

}