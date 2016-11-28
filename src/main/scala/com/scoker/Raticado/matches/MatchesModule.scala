package com.scoker.Raticado.matches

import com.scoker.Raticado.calculator.EloCalculator
import com.scoker.Raticado.player.InMemoryPlayerService
import com.scoker.Raticado.scoring.InMemoryScoringService
import com.scoker.Raticado.services.{MatchService, PlayerService, RankCalculator, ScoringService}
import net.codingwell.scalaguice.ScalaModule

class MatchesModule extends ScalaModule {

  override def configure(): Unit = {
    bind[RankCalculator].toInstance(EloCalculator)
    bind[ScoringService].to[InMemoryScoringService]
    bind[MatchService].to[InMemoryMatchService]
  }
}
