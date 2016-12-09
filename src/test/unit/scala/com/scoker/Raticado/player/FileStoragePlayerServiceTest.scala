package com.scoker.Raticado.player

import java.io.File
import java.nio.file.{Files, Paths}

import com.scoker.Raticado.UnitTest
import com.scoker.Raticado.calculator.Elo
import com.scoker.Raticado.services.{PlayerNotFoundException, PlayerService}

import scala.io.Source

//TODO pomyslec jak polaczyc te testy i InMemory
class FileStoragePlayerServiceTest extends UnitTest with PlayersTestObjects {

  var playerService: PlayerService = _

  val storageFileName = "test-storage.txt"

  before {
    playerService = new FileStoragePlayerService(storageFileName)
  }

  after {
    new File(storageFileName).delete()
  }

  "ListPlayers" should "return empty set when no players were added" in {
    playerService.listPlayers() shouldEqual Set()
  }

  it should "return the same player which has been added when only one player was added" in {
    playerService.addPlayer(Krzychu)
    playerService.listPlayers() shouldEqual Set(Krzychu)
  }

  "AddPlayer" should "create new storing file if it already not exists" in {
    Files.exists(Paths.get(storageFileName)) shouldEqual false
    playerService.addPlayer(AnyPlayer)
    Files.exists(Paths.get(storageFileName)) shouldEqual true
  }

  it should "save player in file with specific format" in {
    playerService.addPlayer(Krzychu)
    val fileLines = Source.fromFile(storageFileName).getLines.toList
    fileLines.size shouldEqual 2
    fileLines.head shouldEqual Krzychu.name.name
    fileLines(1) shouldEqual Krzychu.elo.rank.toString
  }

  it should "handle multiple additions" in {
    playerService.addPlayer(Krzychu)
    playerService.addPlayer(Zgredek)
    playerService.addPlayer(Claudia)
    val fileLines = Source.fromFile(storageFileName).getLines.toList
    fileLines.size shouldEqual 6
    fileLines.head shouldEqual Krzychu.name.name
    fileLines(1) shouldEqual Krzychu.elo.rank.toString
    fileLines(2) shouldEqual Zgredek.name.name
    fileLines(3) shouldEqual Zgredek.elo.rank.toString
    fileLines(4) shouldEqual Claudia.name.name
    fileLines(5) shouldEqual Claudia.elo.rank.toString
  }

  "Get" should "return None when no player was found" in {
    playerService.addPlayer(Krzychu)
    playerService.get(PlayerName("NonExistentName")) shouldEqual None
  }

  it should "return player with specific name when called" in {
    playerService.addPlayer(Krzychu)
    playerService.get(Krzychu.name) shouldEqual Some(Krzychu)
  }

  it should "return player with specific name when more than one player is stored" in {
    playerService.addPlayer(Krzychu)
    playerService.addPlayer(Zgredek)
    playerService.addPlayer(Claudia)
    playerService.get(Zgredek.name) shouldEqual Some(Zgredek)
  }

  "UpdatePlayer" should "update player ranking" in {
    playerService.addPlayer(Krzychu)
    playerService.get(Krzychu.name).get.elo.rank shouldEqual 1500
    playerService.updateRanking(Krzychu.name, 23)
    playerService.get(Krzychu.name).get.elo.rank shouldEqual 1523
  }

  it should "update player when more players are in storage file" in {
    playerService.addPlayer(Krzychu)
    playerService.addPlayer(Zgredek)
    playerService.addPlayer(Claudia)
    playerService.get(Krzychu.name).get.elo.rank shouldEqual 1500
    playerService.get(Zgredek.name).get.elo.rank shouldEqual 1500
    playerService.get(Claudia.name).get.elo.rank shouldEqual 1500
    playerService.updateRanking(Zgredek.name, -17)
    playerService.get(Krzychu.name).get.elo.rank shouldEqual 1500
    playerService.get(Zgredek.name).get.elo.rank shouldEqual 1483
    playerService.get(Claudia.name).get.elo.rank shouldEqual 1500
  }

  it should "throw an exception when trying to update player which is not in storage file" in {
    an [PlayerNotFoundException] should be thrownBy playerService.updateRanking(Krzychu.name, -1)
  }

  "UpdatePlayers" should "update all players in team" in {
    playerService.addPlayer(Krzychu)
    playerService.addPlayer(Zgredek)
    playerService.get(Krzychu.name).get.elo.rank shouldEqual 1500
    playerService.get(Zgredek.name).get.elo.rank shouldEqual 1500
    playerService.updateRankings(KrzychuZgredakTeam, 23)
    playerService.get(Krzychu.name).get.elo.rank shouldEqual 1523
    playerService.get(Zgredek.name).get.elo.rank shouldEqual 1523
  }

  it should "add player if player from team is not in storage file" in {
    playerService.addPlayer(Krzychu)
    playerService.get(Krzychu.name).get.elo.rank shouldEqual 1500
    playerService.updateRankings(KrzychuZgredakTeam, 23)
    playerService.get(Krzychu.name).get.elo.rank shouldEqual 1523
    playerService.get(Zgredek.name).get.elo.rank shouldEqual 1523
  }

  it should "correctly update rankings if players have different rankings" in {
    playerService.addPlayer(Krzychu)
    playerService.addPlayer(Zgredek.copy(elo = Elo(1450)))
    playerService.get(Krzychu.name).get.elo.rank shouldEqual 1500
    playerService.get(Zgredek.name).get.elo.rank shouldEqual 1450
    playerService.updateRankings(KrzychuZgredakTeam, 23)
    playerService.get(Krzychu.name).get.elo.rank shouldEqual 1523
    playerService.get(Zgredek.name).get.elo.rank shouldEqual 1473
  }

  "CreatePlayer" should "add player" in {
    playerService.listPlayers() shouldEqual Set()
    playerService.createPlayer(Krzychu.name)
    playerService.listPlayers() shouldEqual Set(Krzychu)
  }

  it should "throw an exception when player already exists in storage file" in {
    playerService.addPlayer(Krzychu)
    an [PlayerAlreadyExistsException] should be thrownBy playerService.createPlayer(Krzychu.name)
  }

  it should "add player when some players are already in storage file" in {
    playerService.addPlayer(Krzychu)
    playerService.addPlayer(Zgredek)
    playerService.createPlayer(Claudia.name)
    playerService.listPlayers shouldEqual Set(Krzychu, Zgredek, Claudia)
  }
}