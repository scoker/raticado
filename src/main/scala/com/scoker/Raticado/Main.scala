package com.scoker.Raticado

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.Guice
import com.scoker.Raticado.guice.InjectedProps
import spray.can.Http

import util.Properties
import scala.concurrent.duration._

object Raticado extends App {

  implicit private val injector = Guice.createInjector(new MainModule())

  private val props = injector.getInstance(classOf[InjectedProps])
  implicit val system = injector.getInstance(classOf[ActorSystem])

  val serviceActor = system.actorOf(props(classOf[RoutingActor]), "routingServiceActor")
  val port = Properties.envOrElse("PORT", "8080").toInt

  implicit val timeout = Timeout(5.seconds)

  IO(Http) ? Http.Bind(serviceActor, interface = "0.0.0.0", port = port)
}