package framework.actorserver

import scala.collection.immutable.HashMap
import scala.concurrent.Future

/**
  *
  */
trait Mod {
  def init(): State

  def handle[T](request: T, state: State): (Any, State)
}

class MyMod(server: Server) extends Mod {
  override def init(): State = {
    DictState(HashMap())
  }

  override def handle[T](request: T, state: State): (Any, State) = (request, state) match {
    case (('add, name: Symbol, place: String), dict: DictState) =>
      ('ok, DictState(dict.dict + (name -> place)))
    case (('whereis, name: Symbol), dict: DictState) =>
      (dict.dict.get(name), dict)
  }

  def add(name: Symbol, place: String): Future[Symbol] = {
    server.rpc[(Symbol, Symbol, String), Symbol]('MyMod, ('add, name, place))
  }

  def whereid(name: Symbol): Future[Option[String]] = {
    server.rpc[(Symbol, Symbol), Option[String]]('MyMod, ('whereis, name))
  }
}

trait State
case class DictState(dict: HashMap[Symbol, String]) extends State
