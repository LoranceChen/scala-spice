package framework.actorserver

import scala.collection.immutable.HashMap
import scala.concurrent.Future

/**
  *
  */
trait Mod {
  type Result
  type St <: State
  def init(): State

  def handle[T](request: T, state: St): (Result, State)
}

class MyMod(server: Server) extends Mod {
  type St = DictState
  override type Result = Any

  override def init(): State = {
    println("init mod")
    DictState(HashMap())
  }

  override def handle[T](request: T, state: DictState): (Result, State) = (request, state) match {
    case (('add, name: Symbol, place: String), dict) =>
      ('ok, DictState(dict.dict + (name -> place)))
    case (('whereis, name: Symbol), dict) =>
      (dict.dict.get(name), dict)
  }

  def add(name: Symbol, place: String): Future[Symbol] = {
    server.rpc('MyMod, ('add, name, place))
  }

  def whereid(name: Symbol): Future[Option[String]] = {
    server.rpc('MyMod, ('whereis, name))
  }
}

trait State
case class DictState(dict: HashMap[Symbol, String]) extends State
