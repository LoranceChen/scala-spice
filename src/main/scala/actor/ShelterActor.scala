package actor

import actor.ShelterActor.{Cat, CatActor, TCat}
import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.duration._
import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * 暴露方法而不是消息协议给使用者，将actor视为包含了并发逻辑的方法的实现工具。
  *
  * 这里提供最简单直接的调用方式，不足的是，使用者需要通过指定变量名为TCat，从而手动屏蔽CatActor类型
  */
object ShelterActor {

  case class Cat(name: String)

  trait TCat {
    def addCat(cat: Cat): Unit

    def getCat(name: String): Future[Option[Cat]]
  }


  class CatActor extends TCat with Actor {
    implicit val timeout: Timeout = 30 seconds
    var cats = mutable.ArrayBuffer[Cat]()

    def addCat(cat: Cat): Unit = this.self ! cat

    def getCat(name: String): Future[Option[Cat]] = (self ? ('getCatByName, name)).mapTo[Option[Cat]]

    def receive = {
      case ('addCat, cat: Cat) =>
        cats :+ cat
      case ('getCatByName, name) =>
        sender() ! cats.find(_.name == name)
    }
  }


}

object ActorHelper {

  def genActor(implicit actorSystem: ActorSystem) = {
    actorSystem.actorOf(Props[CatActor](), "name")
  }
}

object CatTest extends App {
  implicit val actorRoot = ActorSystem("test")
  val cat = actorRoot.actorOf(Props[CatActor]())

//  cat.addCat(Cat("hi"))
//
//  cat.getCat("hi") foreach(println)

  Thread.currentThread().join()
}
