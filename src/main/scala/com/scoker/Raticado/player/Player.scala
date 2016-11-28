package com.scoker.Raticado.player

import com.scoker.Raticado.calculator.Elo
import spray.json.DefaultJsonProtocol

case class Player(name: PlayerName, elo: Elo = Elo(1500))

object PlayerJsonImplicits extends DefaultJsonProtocol {
  import PlayerNameJsonImplicits._
  import com.scoker.Raticado.calculator.EloJsonImplicits._
  implicit val playerFormat = jsonFormat2(Player)
}

