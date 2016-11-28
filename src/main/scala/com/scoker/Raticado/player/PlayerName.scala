package com.scoker.Raticado.player

import spray.json.DefaultJsonProtocol

case class PlayerName(name: String)

object PlayerNameJsonImplicits extends DefaultJsonProtocol {
  implicit val playerNameFormat = jsonFormat1(PlayerName)
}