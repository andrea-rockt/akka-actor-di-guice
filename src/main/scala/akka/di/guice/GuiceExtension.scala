package akka.di.guice

import akka.actor.{ActorPath, ActorRef, ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import akka.di.Supervisor
import com.google.inject.name.{Named, Names}
import com.google.inject.{AbstractModule, Guice, Injector, Key, Module}

import scala.collection.immutable
import scala.util.{Failure, Success}

trait GuiceExtension extends Extension {
  val injector : Injector
  val supervisor : ActorRef
}

class DefaultGuiceExtension(system: ExtendedActorSystem, modules: Module*) extends GuiceExtension {

  lazy val injector: Injector =
    Guice.createInjector(modules : _*)

  override lazy val supervisor : ActorRef =
    injector.getInstance(Key.get(classOf[ActorRef], classOf[Supervisor]))

}

class ActorSystemProviderModule(actorSystem: ActorSystem) extends AbstractModule {
  override def configure() {
    bind(classOf[ActorSystem]).toInstance(actorSystem)
  }
}

object GuiceExtension extends ExtensionId[GuiceExtension] with ExtensionIdProvider {

  override def createExtension(system: ExtendedActorSystem): GuiceExtension = {

    val config = system.settings.config.atPath("akka.di.guice")

    val moduleFQDN = config.getString("module")

    val noParameters: immutable.Seq[(Class[_], AnyRef)]= immutable.Seq.empty
    val module = system.dynamicAccess.createInstanceFor[Module](moduleFQDN, noParameters) match {
      case Success(m) => m
      case Failure(exception) =>
        system.log.error(s"Could not create application module [{}], exception [{}]", moduleFQDN, exception)
        throw exception
    }

    new DefaultGuiceExtension(system, new ActorSystemProviderModule(system), module)
  }

  override def lookup(): ExtensionId[_ <: Extension] = GuiceExtension

}
