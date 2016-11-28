package com.scoker.Raticado.matches

import com.scoker.Raticado.player.Team
import spray.json.DefaultJsonProtocol

case class Fixture(gameDate: String, matches: List[Match], teams: List[Team])

object FixtureJsonImplicits extends DefaultJsonProtocol {
  import com.scoker.Raticado.player.TeamJsonImplicits._
  import com.scoker.Raticado.matches.MatchJsonImplicits._
  implicit val fixtureFormat = jsonFormat3(Fixture)
}