package com.scoker.Raticado

import java.time.LocalDate

import com.scoker.Raticado.matches.{Fixture, InMemoryMatchService, Match}
import com.scoker.Raticado.player.{InMemoryPlayerService, Player, PlayerName, Team}
import com.scoker.Raticado.services.{MatchService, PlayerService, ScoringService}
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfter, FlatSpecLike, Matchers}
import org.scalatest.mock.MockitoSugar

class MatchServiceWithPlayerServiceTest extends FlatSpecLike with BeforeAndAfter with Matchers with MockitoSugar {

  private var matchService: MatchService = _
  private var playerService: PlayerService = _
  private var mockedScoringService: ScoringService = _

  before {
    playerService = new InMemoryPlayerService
    mockedScoringService = mock[ScoringService]
    matchService = new InMemoryMatchService(playerService, mockedScoringService)
  }

  "AddAllMatches" should "XXX" ignore {
    val matches: List[Match] = List(Match_1, Match_2, Match_3)
    val teams: List[Team] = List(TeamOf4_1, TeamOf4_2)
    val date = LocalDate.now()
    val fixture = Fixture(SomeDate, matches, teams)
    when(mockedScoringService.score(TeamOf4_1, TeamOf4_2, FirstTeamWins)).thenReturn(100)
    when(mockedScoringService.score(TeamOf4_1, TeamOf4_2, Draw)).thenReturn(-30)

    matchService.addAllMatches(fixture)
    matchService.listMatches() shouldEqual List(Match_1, Match_2, Match_3)
    playerService.listPlayers() shouldEqual Set(
      Player(PlayerName("Krzychu")),
      Player(PlayerName("Claudia")),
      Player(PlayerName("Zgredek")),
      Player(PlayerName("Michal")),
      Player(PlayerName("Andriu")),
      Player(PlayerName("Magda")))
  }

  private val SomeDate = "some date"
  private val FirstTeamWins: Double = 1.0
  private val Draw: Double = 0.5

  private val TeamOf4_1 = Team("Team 1", Set("Krzychu", "Claudia", "Zgredek"))
  private val TeamOf4_2 = Team("Team 2", Set("Michal", "Andriu", "Magda"))
  private val Match_1 = Match(TeamOf4_1, TeamOf4_2, 1.0, SomeDate)
  private val Match_2 = Match(TeamOf4_1, TeamOf4_2, 1.0, SomeDate)
  private val Match_3 = Match(TeamOf4_1, TeamOf4_2, 0.5, SomeDate)
}
