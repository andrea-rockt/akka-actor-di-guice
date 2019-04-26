package akka.di.example

import akka.actor.Actor
import akka.di.example.AppSupervisor.Start
import akka.di.guice.ActorDep
import com.google.inject.Inject

object AppSupervisor {
  object Start
}

class AppSupervisor @Inject() (printer: ActorDep[PrinterActor]) extends Actor {

  def receive : Receive = {
    case Start =>
      printer.instance ! PrinterActor.PrintDocumentMsg("job1", collection.immutable.Seq("A", "B", "C"))
      printer.instance ! PrinterActor.PrintDocumentMsg("job2", collection.immutable.Seq("1", "2", "3"))
  }

}
