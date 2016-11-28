package com.scoker.Raticado

import akka.actor.{ActorRef, ActorSystem}
import akka.io.IO
import akka.util.Timeout
import akka.pattern.ask
import com.google.inject.Guice
import com.scoker.Raticado.guice.InjectedProps
import spray.can.Http
import spray.servlet.WebBoot

import scala.concurrent.duration._

class Main extends WebBoot {

  implicit private val injector = Guice.createInjector(new MainModule())

  private val props = injector.getInstance(classOf[InjectedProps])
  override implicit val system = injector.getInstance(classOf[ActorSystem])

  override val serviceActor = system.actorOf(props(classOf[RoutingActor]), "routingServiceActor")
  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(serviceActor, interface = "0.0.0.0", port = 8080)
}
