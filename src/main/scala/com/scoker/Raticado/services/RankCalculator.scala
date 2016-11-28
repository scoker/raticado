package com.scoker.Raticado.services

trait RankCalculator {
  def calculateRankChange(rankingDifference: Int, result: Double): Int
}
