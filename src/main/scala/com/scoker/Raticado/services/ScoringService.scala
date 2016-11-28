package com.scoker.Raticado.services

import com.scoker.Raticado.player.Team

trait ScoringService {

  def score(teamA: Team, teamB: Team, result: Double): Int
}
