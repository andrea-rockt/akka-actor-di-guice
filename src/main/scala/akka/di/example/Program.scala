package akka.di.example

import akka.actor.ActorSystem
import akka.di.guice.GuiceExtension
import com.typesafe.config.ConfigFactory

object Program {
  def main(args: Array[String]): Unit = {

    val moduleConfig = ConfigFactory.parseString("akka.di.guice.module = " + classOf[AppModule].getName)
      .resolveWith(ConfigFactory.load())


    val system = ActorSystem.create("guice-di-example", moduleConfig)
    val injector = GuiceExtension(system).injector


    injector.getInstance(classOf[App]).start()

  }
}
