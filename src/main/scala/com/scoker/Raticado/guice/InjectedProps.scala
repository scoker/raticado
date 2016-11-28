package com.scoker.Raticado.guice

import javax.inject.Inject

import akka.actor.{Actor, Props}
import com.google.inject.Injector

class InjectedProps @Inject()(val injector: Injector) {

  def apply[A <: Actor](actorType: Class[A], args: Any*) = Props(classOf[InjectedActorProducer], injector, actorType, args)

}
