package com.scoker.Raticado.matches

import com.scoker.Raticado.RouteUnitTests
import com.scoker.Raticado.player.Team
import com.scoker.Raticado.services.MatchService
import org.mockito.Mockito._
import spray.http.{ContentTypes, HttpEntity, StatusCodes}

class MatchesRoutesTest extends RouteUnitTests {

  //TODO wydzielic trait z obiektami domenowymi (player, match, ...)
  private var mockedMatchService: MatchService = _
  private var tested: MatchesRoutes = _

  before {
    mockedMatchService = mock[MatchService]
    tested = new MatchesRoutes(mockedMatchService)
  }

  "/matches POST" should "call MatchService to add match" in {
    Post("/matches", HttpEntity(ContentTypes.`application/json`, Match_1_Json)) ~> tested.routes ~> check {
      status shouldEqual StatusCodes.OK
      verify(mockedMatchService, times(1)).addMatch(Match_1)
    }
  }

  "/fixtures POST" should "add all matches in fixture" in {
    Post("/fixtures", HttpEntity(ContentTypes.`application/json`, Fixture_Matches_1_3_Json)) ~> tested.routes ~> check {
      status shouldEqual StatusCodes.OK
      verify(mockedMatchService, times(1)).addAllMatches(Fixture_Matches_1_3)
    }
  }

  private val SomeDate = "some date"
  private val TeamOf4_1 = Team("Team 1", Set("Krzychu", "Claudia", "Zgredek"))
  private val TeamOf4_2 = Team("Team 2", Set("Michal", "Andriu", "Magda"))
  private val Match_1 = Match(TeamOf4_1, TeamOf4_2, 1.0, SomeDate)
  private val Match_2 = Match(TeamOf4_1, TeamOf4_2, 1.0, SomeDate)
  private val Match_3 = Match(TeamOf4_1, TeamOf4_2, 0.5, SomeDate)
  private val Fixture_Matches_1_3 = Fixture(SomeDate, List(Match_1, Match_3), List(TeamOf4_1, TeamOf4_2))
  private val Match_1_Json =
    """{
      |  "teamA": {
      |    "teamName": "Team 1",
      |    "playerNames": ["Krzychu", "Claudia", "Zgredek"]
      |  },
      |  "teamB": {
      |    "teamName": "Team 2",
      |    "playerNames": ["Michal", "Andriu", "Magda"]
      |  },
      |  "result": 1.0,
      |  "date": "some date"
      |  }""".stripMargin
  private val Fixture_Matches_1_3_Json =
    """{
      |  "gameDate": "some date",
      |  "matches": [
      |    {
      |      "teamA": {
      |        "teamName": "Team 1",
      |        "playerNames": ["Krzychu", "Claudia", "Zgredek"]
      |      },
      |      "teamB": {
      |        "teamName": "Team 2",
      |        "playerNames": ["Michal", "Andriu", "Magda"]
      |      },
      |      "result": 1.0,
      |      "date": "some date"
      |      },
      |      {
      |      "teamA": {
      |        "teamName": "Team 1",
      |        "playerNames": ["Krzychu", "Claudia", "Zgredek"]
      |      },
      |      "teamB": {
      |        "teamName": "Team 2",
      |        "playerNames": ["Michal", "Andriu", "Magda"]
      |      },
      |      "result": 0.5,
      |      "date": "some date"
      |      }],
      |      "teams": [
      |      {
      |        "teamName": "Team 1",
      |        "playerNames": ["Krzychu", "Claudia", "Zgredek"]
      |      },
      |      {
      |        "teamName": "Team 2",
      |        "playerNames": ["Michal", "Andriu", "Magda"]
      |      }]
      |  }""".stripMargin
}
