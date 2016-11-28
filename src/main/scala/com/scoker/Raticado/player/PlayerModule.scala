package com.scoker.Raticado.player

import com.scoker.Raticado.services.PlayerService
import net.codingwell.scalaguice.ScalaModule
import javax.inject.Singleton

class PlayerModule extends ScalaModule {

  override def configure(): Unit = {
    bind[PlayerService].to[InMemoryPlayerService].in[Singleton]
  }
}
