package com.scoker.Raticado.matches

import java.time.LocalDate

import com.scoker.Raticado.player.Team
import com.scoker.Raticado.services.{MatchService, PlayerService, ScoringService}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FlatSpecLike, Matchers}

class InMemoryMatchServiceTest extends FlatSpecLike with BeforeAndAfter with Matchers with MockitoSugar {

  private var matchService: MatchService = _
  private var mockedPlayerService: PlayerService = _
  private var mockedScoringService: ScoringService = _
  private val anyMatch = mock[Match]

  before {
    mockedPlayerService = mock[PlayerService]
    mockedScoringService = mock[ScoringService]
    matchService = new InMemoryMatchService(mockedPlayerService, mockedScoringService)
  }

  "ListMatches" should "return empty set when no teams were added" in {
    matchService.listMatches() shouldEqual List()
  }

  it should "return one match if only one match was added" in {
    matchService.addMatch(anyMatch)
    matchService.listMatches() shouldEqual List(anyMatch)
  }

  it should "return matches in order which they have been added" in {
    matchService.addMatch(Match_1)
    matchService.addMatch(Match_2)
    matchService.addMatch(Match_3)
    matchService.listMatches() shouldEqual List(Match_1, Match_2, Match_3)
  }

  "AddMatch" should "update players rankings" in {
    when(mockedScoringService.score(TeamA, TeamB, FirstTeamWins)).thenReturn(100)
    matchService.addMatch(aMatch)
    verify(mockedPlayerService, times(1)).updateRankings(aMatch.teamA, 100)
    verify(mockedPlayerService, times(1)).updateRankings(aMatch.teamB, -100)
  }

  it should "update players rankings when second team wins" in {
    when(mockedScoringService.score(TeamA, TeamB, SecondTeamWins)).thenReturn(-100)
    matchService.addMatch(aMatch2ndTeamWins)
    verify(mockedPlayerService, times(1)).updateRankings(aMatch2ndTeamWins.teamA, -100)
    verify(mockedPlayerService, times(1)).updateRankings(aMatch2ndTeamWins.teamB, 100)
  }

  "AddMatch" should "not create any match when Fixture is empty" in {
    val matches: List[Match] = List()
    val teams: List[Team] = List(TeamOf4_1, TeamOf4_2)
    val date = LocalDate.now()
    val fixture = Fixture(SomeDate, matches, teams)
    matchService.addAllMatches(fixture)
    matchService.listMatches() shouldEqual List()
  }

  "AddAllMatches" should "create all matches contained in Fixture" in {
    val matches: List[Match] = List(Match_1, Match_2, Match_3)
    val teams: List[Team] = List(TeamOf4_1, TeamOf4_2)
    val date = LocalDate.now()
    val fixture = Fixture(SomeDate, matches, teams)
    when(mockedScoringService.score(TeamOf4_1, TeamOf4_2, FirstTeamWins)).thenReturn(100)
    when(mockedScoringService.score(TeamOf4_1, TeamOf4_2, Draw)).thenReturn(80)
    when(mockedScoringService.score(TeamOf4_1, TeamOf4_2, Draw)).thenReturn(-30)

    matchService.addAllMatches(fixture)
    matchService.listMatches() shouldEqual List(Match_1, Match_2, Match_3)
  }

  private val SomeDate = "some date"
  private val FirstTeamWins: Double = 1.0
  private val SecondTeamWins: Double = 0.0
  private val Draw: Double = 0.5
  private val TeamA = Team("Team A", Set("Krzychu"))
  private val TeamB = Team("Team B", Set("Zgredek"))
  private val aMatch = Match(TeamA, TeamB, FirstTeamWins, SomeDate)
  private val aMatch2ndTeamWins = Match(TeamA, TeamB, SecondTeamWins, SomeDate)

  private val TeamOf4_1 = Team("Team 1", Set("Krzychu", "Claudia", "Zgredek"))
  private val TeamOf4_2 = Team("Team 2", Set("Michal", "Andriu", "Magda"))
  private val Match_1 = Match(TeamOf4_1, TeamOf4_2, 1.0, SomeDate)
  private val Match_2 = Match(TeamOf4_1, TeamOf4_2, 1.0, SomeDate)
  private val Match_3 = Match(TeamOf4_1, TeamOf4_2, 0.5, SomeDate)
}
