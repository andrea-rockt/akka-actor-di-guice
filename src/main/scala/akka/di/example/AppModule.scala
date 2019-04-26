package akka.di.example

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.di.Supervisor
import akka.di.guice.{ActorDep, ActorFactory, GuiceActorDep, GuiceActorFactory, GuiceIndirectActorProducer}
import com.google.inject._

class AppModule extends AbstractModule {
  override def configure(): Unit = {

    // Other components
    bind(classOf[PrinterService]).to(classOf[PrinterServiceImpl]).in(classOf[Singleton])

    // Bind actors
    bind(classOf[PrintJobActor])
    bind(classOf[PrinterActor])

  }

  @Provides
  @Singleton
  def providePrinterActor(actorSystem: ActorSystem): ActorDep[PrinterActor] = {
    new GuiceActorDep[PrinterActor](actorSystem)
  }

  @Provides
  @Supervisor
  @Singleton
  def provideSupervisor(actorSystem: ActorSystem): ActorRef = {
    actorSystem.actorOf(Props(classOf[GuiceIndirectActorProducer], classOf[AppSupervisor]), "app-supervisor")
  }

  @Provides
  @Singleton
  def providePrinterJobFactory(): ActorFactory[PrintJobActor] = {
    new GuiceActorFactory[PrintJobActor]()
  }
}
