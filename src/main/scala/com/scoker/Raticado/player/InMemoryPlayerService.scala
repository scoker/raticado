package com.scoker.Raticado.player

import com.scoker.Raticado.calculator.Elo
import com.scoker.Raticado.services.PlayerService

class InMemoryPlayerService extends PlayerService {

  private var players: Set[Player] = Set()

  override def listPlayers(): Set[Player] = players

  override def addPlayer(player: Player): Player = {
    throwAnExceptionIfPlayerExists(player.name)
    players = players + player
    player
  }

  override def updateRanking(playerName: PlayerName, rankDelta: Int): Player = {
    val toUpdatePlayer = get(playerName).getOrElse(throw PlayerNotFoundException(playerName.name))
    val updatedPlayer = Player(toUpdatePlayer.name, Elo(toUpdatePlayer.elo.rank + rankDelta))
    players = players - toUpdatePlayer
    players = players + updatedPlayer
    get(updatedPlayer.name).get
  }

  override def updateRankings(team: Team, rankDelta: Int): Team = {
    team.playerNames.map { name => get(PlayerName(name)).getOrElse(createPlayer(PlayerName(name)))}
    team.playerNames.flatMap(name => get(PlayerName(name))).foreach{ player =>
      updateRanking(player.name, rankDelta)
      ()
    }
    Team(team.teamName, team.playerNames.filter(name => get(PlayerName(name)).isDefined))
  }

  override def get(name: PlayerName): Option[Player] = players.find(player => player.name.equals(name))

  override def createPlayer(name: PlayerName): Player = {
    throwAnExceptionIfPlayerExists(name)
    val newPlayer = Player(name)
    addPlayer(newPlayer)
    newPlayer
  }

  private def throwAnExceptionIfPlayerExists(playerName: PlayerName) = {
    if(get(playerName).isDefined) throw PlayerAlreadyExistsException(playerName.name)
  }
}

case class PlayerNotFoundException(playerName: String) extends Exception(s"Player: $playerName was not found in system.")
case class PlayerAlreadyExistsException(playerName: String) extends Exception(s"Player: $playerName is already in the system.")

