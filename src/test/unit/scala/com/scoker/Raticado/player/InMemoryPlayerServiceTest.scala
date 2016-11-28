package com.scoker.Raticado.player

import com.scoker.Raticado.calculator.Elo
import com.scoker.Raticado.services.PlayerService
import org.scalatest.{BeforeAndAfter, FlatSpecLike, Matchers}

class InMemoryPlayerServiceTest extends FlatSpecLike with BeforeAndAfter with Matchers {

  private var playerService: PlayerService = _

  private val Krzychu = Player(PlayerName("Krzychu"))
  private val Andriu = Player(PlayerName("Andriu"))
  private val Zgredek = Player(PlayerName("Zgredek"))
  private val Claudia = Player(PlayerName("Claudia"))

  before {
    playerService = new InMemoryPlayerService
  }

  "Get" should "return None when no player was found" in {
    playerService.get(PlayerName("NonExistentName")) shouldEqual None
  }

  "ListPlayers" should "return empty set if player was not added" in {
    playerService.listPlayers() shouldEqual Set()
  }

  it should "return one player when only one has been added" in {
    playerService.addPlayer(Krzychu)
    playerService.listPlayers() shouldEqual Set(Krzychu)
  }

  it should "return all previously added players" in {
    playerService.addPlayer(Krzychu)
    playerService.listPlayers() shouldEqual Set(Krzychu)
    playerService.addPlayer(Andriu)
    playerService.listPlayers() shouldEqual Set(Krzychu, Andriu)
    playerService.addPlayer(Zgredek)
    playerService.listPlayers() shouldEqual Set(Krzychu, Andriu, Zgredek)
  }

  it should "not be modifiable outside service" in {
    playerService.addPlayer(Krzychu)
    var result = playerService.listPlayers()
    result = result + Zgredek
    playerService.listPlayers() shouldEqual Set(Krzychu)
  }

  "UpdateRanking" should "add 100 to Krzychu ranking in system" in {
    playerService.addPlayer(Krzychu)
    playerService.updateRanking(Krzychu.name, hundredPoints)
    playerService.get(Krzychu.name) shouldEqual Some(Player(PlayerName("Krzychu"), Elo(1600)))
  }

  it should "PlayerNotFoundException when given player does not exists in system" in {
    an [PlayerNotFoundException] should be thrownBy playerService.updateRanking(Krzychu.name, hundredPoints)
  }

  "UpdateRankings" should "add 100 to Krzychu ranking in system" in {
    playerService.addPlayer(Krzychu)
    playerService.updateRankings(Team("", Set("Krzychu")), hundredPoints)
    playerService.get(Krzychu.name) shouldEqual Some(Player(PlayerName("Krzychu"), Elo(1600)))
  }

  it should "add non-existent players to system" in {
    playerService.addPlayer(Krzychu)
    playerService.addPlayer(Andriu)
    playerService.updateRankings(teamOfFour, 0) shouldEqual teamOfFour
    playerService.listPlayers() shouldEqual Set(Krzychu, Andriu, Zgredek, Claudia)
  }

  it should "compute ranking for player who has been not added to system" in {
    playerService.addPlayer(Andriu)
    playerService.updateRankings(Team("One Man Team", Set(Krzychu.name.name)), hundredPoints) shouldEqual Team("One Man Team", Set(Krzychu.name.name))
    playerService.get(Krzychu.name) shouldEqual Some(Krzychu.copy(elo = Elo(1600)))
  }

  it should "handle more difficult use case" in {
    val betterKrzychu = Player(Krzychu.name, Elo(1700))
    playerService.addPlayer(betterKrzychu)
    playerService.addPlayer(Andriu)
    playerService.addPlayer(Zgredek)

    playerService.updateRankings(teamOfFour, 30)
    playerService.get(Krzychu.name) shouldEqual Some(Player(Krzychu.name, Elo(1730)))
    playerService.get(Andriu.name) shouldEqual Some(Player(Andriu.name, Elo(1530)))
    playerService.get(Zgredek.name) shouldEqual Some(Player(Zgredek.name, Elo(1530)))

    playerService.updateRankings(teamOfFour, -50)
    playerService.get(Krzychu.name) shouldEqual Some(Player(Krzychu.name, Elo(1680)))
    playerService.get(Andriu.name) shouldEqual Some(Player(Andriu.name, Elo(1480)))
    playerService.get(Zgredek.name) shouldEqual Some(Player(Zgredek.name, Elo(1480)))
  }

  "CreatePlayer" should "create new player from name with default rank" in {
    playerService.createPlayer(PlayerName("Krzychu"))
    playerService.listPlayers() shouldEqual Set(Player(PlayerName("Krzychu"), Elo(1500)))
  }

  it should "throw an exception when trying to create player which already exists" in {
    playerService.addPlayer(Krzychu)
    an [PlayerAlreadyExistsException] should be thrownBy playerService.createPlayer(Krzychu.name)
  }

  "AddPlayer" should "throw an exception when trying to add player which already exists" in {
    playerService.addPlayer(Krzychu)
    an [PlayerAlreadyExistsException] should be thrownBy playerService.addPlayer(Krzychu)
  }

  private val hundredPoints: Int = 100
  private val teamOfFour = Team("4 mambers team", Set("Krzychu", "Andriu", "Zgredek", "Claudia"))
}
