package com.scoker.Raticado.calculator

import com.scoker.Raticado.services.RankCalculator

object EloCalculator extends RankCalculator {
  private val defaultKFactor = 40

  override def calculateRankChange(rankingDifference: Int, result: Double): Int = calculateRankChange(rankingDifference, result, defaultKFactor)

  private def calculateRankChange(rankingDifference: Int, result: Double, kFactor: Double): Int = {
    var winningProbability = 0.5
    if(rankingDifference < 0) {
      winningProbability = getWinningProbabilityForBetterPlayer(-rankingDifference)
      (kFactor * (result + winningProbability - 1)).toInt
    } else {
      winningProbability = getWinningProbabilityForBetterPlayer(rankingDifference)
      (kFactor * (result - winningProbability)).toInt
    }
  }

  private def getWinningProbabilityForBetterPlayer(difference: Int) = difference match {
    case x if x >= 0 && x < 25 => 0.5
    case x if x >= 25 && x < 50 => 0.57
    case x if x >= 50 && x < 100 => 0.64
    case x if x >= 100 && x < 150 => 0.70
    case x if x >= 150 && x < 200 => 0.76
    case x if x >= 250 && x < 300 => 0.81
    case x if x >= 300 && x < 350 => 0.85
    case x if x >= 350 && x < 400 => 0.89
    case x if x >= 400 && x < 450 => 0.92
    case x if x >= 450 && x < 500 => 0.94
    case x if x >= 500 && x < 735 => 0.96
    case x if x >= 735 => 0.99
  }
}
