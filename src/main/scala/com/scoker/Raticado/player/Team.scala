package com.scoker.Raticado.player

import spray.json.DefaultJsonProtocol

case class Team(teamName: String, playerNames: Set[String])

object TeamJsonImplicits extends DefaultJsonProtocol {
  implicit val teamFormat = jsonFormat2(Team)
}
