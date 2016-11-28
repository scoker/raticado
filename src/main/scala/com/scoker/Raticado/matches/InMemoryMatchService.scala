package com.scoker.Raticado.matches

import com.google.inject.Inject
import com.scoker.Raticado.services.{MatchService, PlayerService, ScoringService}

class InMemoryMatchService @Inject()(playerService: PlayerService, scoringService: ScoringService) extends MatchService {

  private var matches: List[Match] = List()

  override def addMatch(aMatch: Match): Unit = {
    matches = matches :+ aMatch
    val scoreForTeamA = scoringService.score(aMatch.teamA, aMatch.teamB, aMatch.result)

    playerService.updateRankings(aMatch.teamA, scoreForTeamA)
    playerService.updateRankings(aMatch.teamB, -scoreForTeamA)
  }

  override def addAllMatches(fixture: Fixture): Unit = fixture.matches.foreach(aMatch => addMatch(aMatch))

  override def listMatches(): List[Match] = matches
}
