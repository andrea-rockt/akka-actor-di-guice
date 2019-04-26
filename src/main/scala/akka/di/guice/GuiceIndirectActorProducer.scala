package akka.di.guice

import akka.actor.{Actor, ActorCell, IndirectActorProducer}

class GuiceIndirectActorProducer(ac: Class[_ <: Actor]) extends IndirectActorProducer {
  override def produce: Actor = {
    GuiceExtension(ActorCell.contextStack.get().head.system).injector.getInstance(ac)
  }
  override def actorClass: Class[_ <: Actor] = ac
}
