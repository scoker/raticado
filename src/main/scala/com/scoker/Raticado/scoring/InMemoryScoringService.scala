package com.scoker.Raticado.scoring

import com.google.inject.Inject
import com.scoker.Raticado.player.{Player, PlayerName, Team}
import com.scoker.Raticado.services.{PlayerService, RankCalculator, ScoringService}

class InMemoryScoringService @Inject() (rankCalculator: RankCalculator, playerService: PlayerService) extends ScoringService {

  //TODO rozwazyc pozbycie sie rankCalculator z pola i zamiast dac do score calculator: (Int, Double) => Int
  override def score(teamA: Team, teamB: Team, result: Double): Int = {
    val rankDifference = calculateRankDifference(teamA, teamB)
    rankCalculator.calculateRankChange(rankDifference, result)
  }

  private def calculateRankDifference(teamA: Team, teamB: Team) = calculateTeamAverage(teamA) - calculateTeamAverage(teamB)

  private def calculateTeamAverage(team: Team): Int = fetchPlayers(team).toList.map { player => player.elo.rank }.sum / team.playerNames.size

  private def fetchPlayers(team: Team): Set[Player] = {
    team.playerNames.map(name => {
      playerService.get(PlayerName(name)).getOrElse(playerService.createPlayer(PlayerName(name)))
    })
  }
}