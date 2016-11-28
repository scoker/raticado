package com.scoker.Raticado.calculator

import com.scoker.Raticado.services.RankCalculator
import org.scalatest.{BeforeAndAfter, FlatSpecLike, Matchers}

class EloCalculatorTest extends FlatSpecLike with BeforeAndAfter with Matchers {

  private val calculator: RankCalculator = EloCalculator

  //TODO dopisac wszystkie test case'y
  //TODO napisac testy na jak rankDiffre
  //TODO przepisac testy by byly niezalezne od wspolczynnika K
  "Draw" should "have no impact if opponent has same ranking" in {
    val expectedResult = 0
    val result = calculator.calculateRankChange(0, 0.5)
    result shouldEqual expectedResult
  }

  it should "have no impact if opponent is rated little lower" in {
    val expectedResult = 0
    val result = calculator.calculateRankChange(20, 0.5)
    result shouldEqual expectedResult
  }

  it should "be much more harmful if opponent is rated much lower" in {
    val expectedResult = -16
    val result = calculator.calculateRankChange(400, 0.5)
    result shouldEqual expectedResult
  }

  "Win" should "add points if opponent has same ranking" in {
    val expectedResult = 20
    val result = calculator.calculateRankChange(0, 1)
    result shouldEqual expectedResult
  }

  it should "add points if opponent has lower ranking" in {
    val expectedResult = 12
    val result = calculator.calculateRankChange(100, 1)
    result shouldEqual expectedResult
  }

  it should "add less points if opponent has much lower ranking" in {
    val expectedResult = 3
    val result = calculator.calculateRankChange(400, 1)
    result shouldEqual expectedResult
  }

  "Loss" should "subtract points if opponent has same ranking" in {
    val expectedResult = -20
    val result = calculator.calculateRankChange(0, 0.0)
    result shouldEqual expectedResult
  }

  it should "subtract more points if opponent has lower ranking" in {
    val expectedResult = -28
    val result = calculator.calculateRankChange(100, 0.0)
    result shouldEqual expectedResult
  }

  it should "subtract even more points if opponent has much lower ranking" in {
    val expectedResult = -36
    val result = calculator.calculateRankChange(400, 0.0)
    result shouldEqual expectedResult
  }
}
