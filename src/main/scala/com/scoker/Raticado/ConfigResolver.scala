package com.scoker.Raticado

import com.typesafe.config.{Config, ConfigFactory}

object ConfigResolver {
  lazy val config: Config = {
    val configFile: String = "application.conf"
    ConfigFactory.load(configFile)
  }
}
