package akka.di.guice

import akka.actor.{Actor, ActorRef}

trait ActorDep[T <: Actor] {
  val instance: ActorRef
}
