package com.scoker.Raticado

import javax.inject.Singleton

import akka.actor.ActorSystem
import com.google.inject.Provides
import com.scoker.Raticado.matches.MatchesModule
import com.scoker.Raticado.player.PlayerModule
import com.typesafe.config.Config
import net.codingwell.scalaguice.ScalaModule

class MainModule extends ScalaModule {

  override def configure(): Unit = {
    install(new PlayerModule)
    install(new MatchesModule)

    bind[Config].toInstance(ConfigResolver.config)
  }

  @Provides
  @Singleton
  def actorSystem(config: Config): ActorSystem = ActorSystem("raticado", config)
}
