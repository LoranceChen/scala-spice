//package framework.actorserver
//
////import akka.actor.{Actor, ActorRef, ActorSystem, Props, Stash}
//import akka.typed.{ActorRef, ActorSystem, Behavior}
//import akka.typed.scaladsl.{Actor, ActorContext}
//import ErlangStyle._
//
///**
//  * %% this is Erlang
//  **
//  *-module(scaling).
//  *-export([start/1, init/1]).
//  **
//  *start(Downstream) ->
//  *spawn(accumulator, init, [Downstream]).
//  **
//  *init(Downstream) ->
//  *receive
//  *{scale_factor, Factor} ->
//  *loop(Downstream, Factor)
//  *end.
//  **
//  *loop(Downstream, Factor) ->
//  *receive
//  *{measurement, Value} ->
//  *Downstream ! {scaled_measurement, Value * Factor},
//  *loop(Downstream, Factor);
//  **
// *{scale_factor, NewFactor} ->
//  *loop(Downstream, NewFactor);
//  **
// *_ ->
//  *loop(Downstream, Factor)
//  *end.
//  */
//
//object ErlangStyleTest {
//  ActorSystem.create(Actor.deferred(implicit x => {
//      val a = new ScalingOriginalStyle[Any]() {})
//      a.start(null)
//      a.init(null)
//    },
//    "")
//}
//
//trait ScalingOriginalStyle[T] {
//  implicit val ctx: ActorContext[T]
//
//  case class ScaleFactor(factor: Double)
//  case class Measurement(value: Double)
//  case class ScaledMeasurement(value: Double)
//  def start(downstream: ActorRef[Any]) = actor(init(downstream))
//
//  def init(downStream: ActorRef[Any]): Behavior[Any] = {
//    loop(downStream, 0d)
//  }
//
//  def loop(downstream: ActorRef[Any], factor: Double ): Behavior[Any] = {
//    Actor.immutable { (ctx, message) =>
//      message match {
//        case Measurement(value) =>
//          downstream ! ScaledMeasurement(value * factor)
//          loop(downstream, factor)
//        case ScaleFactor(newFactor) =>
//          loop(downstream, newFactor)
//        case _ =>
//          loop(downstream, factor)
//      }
//    }
//  }
//}
//
//object ErlangStyle {
//  def actor[T](action: Behavior[T])(implicit ctx: ActorContext[T]) = {
//    ctx.spawn(action, "somename")
//  }
//}
