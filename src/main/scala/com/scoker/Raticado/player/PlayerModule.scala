package com.scoker.Raticado.player

import com.scoker.Raticado.services.PlayerService
import net.codingwell.scalaguice.ScalaModule
import javax.inject.Singleton

import com.google.inject.name.Names

class PlayerModule extends ScalaModule {

  override def configure(): Unit = {
    bind(classOf[String]).annotatedWith(Names.named("fileName")).toInstance("storage.txt")
    bind[PlayerService].to[FileStoragePlayerService].in[Singleton]
  }
}
