package akka.di.example

import akka.actor.ActorSystem
import akka.di.guice.{ActorDep, ActorFactory, GuiceActorDep, GuiceActorFactory}
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
  @Singleton
  def providePrinterJobFactory(): ActorFactory[PrintJobActor] = {
    new GuiceActorFactory[PrintJobActor]()
  }
}
