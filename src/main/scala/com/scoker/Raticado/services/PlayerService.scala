package com.scoker.Raticado.services

import com.scoker.Raticado.player.{Player, PlayerName, Team}

trait PlayerService {

  def listPlayers(): Set[Player]
  def addPlayer(player: Player): Player
  def updateRankings(team: Team, rankDelta: Int): Team
  def updateRanking(name: PlayerName, rankDelta: Int): Player
  def get(name: PlayerName): Option[Player]
  def createPlayer(name: PlayerName): Player

}

case class PlayerNotFoundException(playerName: String) extends Exception(s"Player: $playerName was not found in system.")
