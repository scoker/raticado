package com.scoker.Raticado.scoring

import com.scoker.Raticado.calculator.Elo
import com.scoker.Raticado.player.{Player, PlayerName, Team}
import com.scoker.Raticado.services.{PlayerService, RankCalculator, ScoringService}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FlatSpecLike, Matchers}

class InMemoryScoringServiceTest extends FlatSpecLike with BeforeAndAfter with Matchers with MockitoSugar {

  private var mockedRankCalculator: RankCalculator = _
  private var mockedPlayerService: PlayerService = _
  private var tested: ScoringService = _

  before {
    mockedRankCalculator = mock[RankCalculator]
    mockedPlayerService = mock[PlayerService]
    tested = new InMemoryScoringService(mockedRankCalculator, mockedPlayerService)
    initializePlayerServiceMock()
  }

  private def initializePlayerServiceMock(): Unit = {
    when(mockedPlayerService.get(PlayerName("Krzychu"))).thenReturn(Some(Player(PlayerName("Krzychu"))))
    when(mockedPlayerService.get(PlayerName("Andriu"))).thenReturn(Some(Player(PlayerName("Andriu"))))
    when(mockedPlayerService.get(PlayerName("Claudia"))).thenReturn(Some(Player(PlayerName("Claudia"))))
    when(mockedPlayerService.get(PlayerName("Ania"))).thenReturn(Some(Player(PlayerName("Ania"))))
  }

  //TODO napisac test, ze scoreservice dodaje gracza jak nie ma go w systemie
  "Score for even teams" should "call rankCalculator with 0 rank difference and win result" in {
    tested.score(TeamA, TeamB, 1.0)
    verify(mockedRankCalculator, times(1)).calculateRankChange(0, 1.0)
  }

  it should "call rankCalculator with 0 rank difference and draw result" in {
    tested.score(TeamA, TeamB, 0.5)
    verify(mockedRankCalculator, times(1)).calculateRankChange(0, 0.5)
  }

  it should "call rankCalculator with 0 rank difference and lose result" in {
    tested.score(TeamA, TeamB, 0.0)
    verify(mockedRankCalculator, times(1)).calculateRankChange(0, 0.0)
  }

  "Score" should "correctly calculate rank difference and call calculator when first team is higher ranked" in {
    when(mockedPlayerService.get(PlayerName("Krzychu"))).thenReturn(Some(Player(PlayerName("Krzychu"), Elo(1600))))
    when(mockedPlayerService.get(PlayerName("Andriu"))).thenReturn(Some(Player(PlayerName("Andriu"), Elo(1600))))
    val teamA = Team("TeamA", Set("Krzychu", "Andriu"))
    tested.score(teamA, TeamB, 1.0)
    verify(mockedRankCalculator, times(1)).calculateRankChange(100, 1.0)
  }

  it should "correctly calculate rank difference and call calculator when second team is higher ranked" in {
    when(mockedPlayerService.get(PlayerName("Krzychu"))).thenReturn(Some(Player(PlayerName("Krzychu"), Elo(1350))))
    when(mockedPlayerService.get(PlayerName("Andriu"))).thenReturn(Some(Player(PlayerName("Andriu"), Elo(1350))))
    val teamA = Team("TeamA", Set("Krzychu", "Andriu"))
    tested.score(teamA, TeamB, 1.0)
    verify(mockedRankCalculator, times(1)).calculateRankChange(-150, 1.0)
  }

  it should "call calculator with correct values if teams are uneven" in {
    tested.score(TeamA, Team("Team of 1", Set("Claudia")), 1.0)
    verify(mockedRankCalculator, times(1)).calculateRankChange(0, 1.0)
  }

  private val TeamA: Team = Team("TeamA", Set("Krzychu", "Andriu"))
  private val TeamB: Team = Team("TeamB", Set("Claudia", "Ania"))
}
