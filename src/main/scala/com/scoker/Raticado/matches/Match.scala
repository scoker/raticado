package com.scoker.Raticado.matches

import com.scoker.Raticado.player.Team
import spray.json.DefaultJsonProtocol

case class Match(teamA: Team, teamB: Team, result: Double, date: String)

object MatchJsonImplicits extends DefaultJsonProtocol {
  import com.scoker.Raticado.player.TeamJsonImplicits._
  implicit val matchFormat = jsonFormat4(Match)
}