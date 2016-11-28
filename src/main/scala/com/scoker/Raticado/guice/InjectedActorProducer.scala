package com.scoker.Raticado.guice

import akka.actor.{Actor, IndirectActorProducer}
import com.google.inject.Injector

/**
 * This actor producer is dedicated for creating actors, which have only one constructor.
 */
class InjectedActorProducer(val injector: Injector, val actorType: Class[_ <: Actor], val args: Any*) extends IndirectActorProducer {

  override def actorClass: Class[_ <: Actor] = actorType

  override def produce(): Actor = {
    val actor = createRawInstanceOfActor
    injector.injectMembers(actor)
    actor
  }

  private def createRawInstanceOfActor = {
    checkIfActorHasOnlyOneConstructor()
    val constructor = actorType.getConstructors()(0)
    try {
      constructor.newInstance(args.asInstanceOf[Seq[AnyRef]]: _*).asInstanceOf[Actor]
    } catch {
      case e: IllegalArgumentException => throw new IllegalArgumentException(s"Actor ${actorType.getCanonicalName} - ${e.getMessage}")
    }
  }

  private def checkIfActorHasOnlyOneConstructor() = {
    val constructors = actorType.getConstructors
    if (constructors.length != 1) {
      throw new ActorProducerException("Actors must have only one constructor")
    }
  }

}
