package com.scoker.Raticado

import com.scoker.Raticado.matches.{Fixture, InMemoryMatchService, Match}
import com.scoker.Raticado.calculator.EloCalculator
import com.scoker.Raticado.player.{InMemoryPlayerService, Team}
import com.scoker.Raticado.scoring.InMemoryScoringService

object RaticadoConsoleTest { //extends App {
  //TODO zmienic to na test akceptacyjny
  private val someDate: String = "some date"

  val Team1 = Team("Pierwszy zespol", Set("Albert Kawiorski", "Radoslaw Morytko", "Grzegorz Wilaszek", "Bartlomiej Fiter"))
  val Team2 = Team("Drugi zespol", Set("Krzysztof Andrzejczak", "Michal Adamek", "Lukasz Potepa", "Wojciech Wasik"))
  val Team3 = Team("Trzeci zespol", Set("Adrian Lachowski", "Maciej Slawinski", "Andrzej Bedychaj", "Tomasz Borowiec"))

  val playerService2 = new InMemoryPlayerService
  val scoringService2 = new InMemoryScoringService(EloCalculator, playerService2)
  val matchService2 = new InMemoryMatchService(playerService2, scoringService2)

  val matches_08_11 = List(
    Match(Team2, Team3, 0.5, "1"),
    Match(Team3, Team1, 0.0, "2"),
    Match(Team1, Team2, 0.0, "3"),
    Match(Team2, Team3, 0.0, "4"),
    Match(Team1, Team3, 1.0, "5"),
    Match(Team1, Team2, 0.0, "6"),
    Match(Team3, Team2, 0.5, "7"),
    Match(Team3, Team1, 0.5, "8"),
    Match(Team1, Team2, 0.5, "9"),
    Match(Team2, Team3, 0.5, "10"),
    Match(Team1, Team3, 0.0, "11"),
    Match(Team2, Team3, 1.0, "12"),
    Match(Team1, Team2, 0.0, "13")
  )
  val fixture_08_11 = Fixture("08/11/2016", matches_08_11, List(Team1, Team2, Team3))

  val TeamA = Team("Pierwszy zespol", Set("Krzysztof Andrzejczak", "Piotr Brankiewicz", "Maciej Slawinski", "Bartlomiej Fiter"))
  val TeamB = Team("Drugi zespol", Set("Andrzej Pozlutko", "Michal Adamek", "Michal Gurgul", "Jakub Piechnik"))
  val TeamC = Team("Trzeci zespol", Set("Radoslaw Morytko", "Lukasz Potepa", "Andrzej Bedychaj"))

  val matches_15_11 = List(
    Match(TeamA, TeamC, 0.5, "1"),
    Match(TeamA, TeamB, 1.0, "2"),
    Match(TeamA, TeamC, 0.5, "3"),
    Match(TeamB, TeamC, 0.5, "4"),
    Match(TeamA, TeamB, 1.0, "5"),
    Match(TeamA, TeamC, 0.5, "6"),
    Match(TeamB, TeamC, 0.0, "7"))
  val fixture_15_11 = Fixture("15/11/2016", matches_15_11, List(TeamA, TeamB, TeamC))

  val Uno = Team("Pierwszy zespol", Set("Krzysztof Andrzejczak", "Lukasz Potepa", "Bartlomiej Fiter", "Michal Gurgul"))
  val Dos = Team("Drugi zespol", Set("Andrzej Pozlutko", "Michal Adamek", "Wojciech Wasik", "Jakub Piechnik"))
  val Tres = Team("Trzeci zespol", Set("Adrian Lachowski", "Piotr Brankiewicz", "Albert Kawiorski", "Andrzej Bedychaj"))

  val matches_22_11 = List(
    Match(Dos, Tres, 0.5, "1"),
    Match(Uno, Tres, 1.0, "2"),
    Match(Uno, Dos, 0.0, "3"),
    Match(Dos, Tres, 0.0, "4"),
    Match(Uno, Tres, 0.0, "5"),
    Match(Dos, Tres, 1.0, "6"),
    Match(Uno, Dos, 0.5, "7"),
    Match(Uno, Tres, 0.0, "8"),
    Match(Dos, Tres, 0.0, "9"),
    Match(Uno, Tres, 0.5, "10"),
    Match(Uno, Dos, 1.0, "11"),
    Match(Uno, Tres, 1.0, "12"),
    Match(Uno, Dos, 0.0, "13"),
    Match(Tres, Dos, 0.0, "14"))
  val fixture_22_11 = Fixture("22/11/2016", matches_22_11, List(Uno, Dos, Tres))

  printAllRankings()
  matchService2.addAllMatches(fixture_08_11)
  println("")
  println("Po grze: " + fixture_08_11.gameDate)
  println("")
  printAllRankings()
  matchService2.addAllMatches(fixture_15_11)
  println("")
  println("Po grze: " + fixture_15_11.gameDate)
  println("")
  printAllRankings()
  matchService2.addAllMatches(fixture_22_11)
  println("")
  println("Po grze: " + fixture_22_11.gameDate)
  println("")
  printAllRankings()


  def printAllRankings() = {
    playerService2.listPlayers().toList.sortBy(-_.elo.rank).foreach(player => print(player.name.name + ": " + player.elo.rank + "\n"))
  }
}
