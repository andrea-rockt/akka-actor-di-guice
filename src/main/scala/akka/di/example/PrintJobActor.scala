package akka.di.example

import akka.actor.Actor
import akka.di.example.PrintJobActor.EndJobMsg
import com.google.inject.Inject

object PrintJobActor {
  case object EndJobMsg
}

class PrintJobActor @Inject()(printerService: PrinterService) extends Actor {
  def receive: Receive = {
    case line: String => printerService.print(line)
    case EndJobMsg => context.stop(self)
  }
}
