package com.scoker.Raticado.player

import java.nio.file.{Files, Paths, StandardOpenOption}
import java.nio.charset.StandardCharsets
import javax.inject.Named

import com.google.inject.Inject
import com.scoker.Raticado.calculator.Elo

import com.scoker.Raticado.services.{PlayerNotFoundException, PlayerService}

import scala.util.{Failure, Success, Try}

class FileStoragePlayerService @Inject() (@Named("fileName") storageFileName: String) extends PlayerService {

  private val StorageFilePath = Paths.get(if(storageFileName.isEmpty) "storage.txt" else storageFileName)

  override def listPlayers(): Set[Player] = {
    Try {
      getPlayersFromDatabase
    } match {
      case Success(players) => players.toSet
      case Failure(f) => Set()
    }
  }

  override def addPlayer(player: Player): Player = {
    write(toWriteFormat(player))
    player
  }

  override def updateRankings(team: Team, rankDelta: Int): Team = {
    team.playerNames.map { name => get(PlayerName(name)).getOrElse(createPlayer(PlayerName(name)))}
    team.playerNames.flatMap(name => get(PlayerName(name))).foreach{ player =>
      updateRanking(player.name, rankDelta)
      ()
    }
    Team(team.teamName, team.playerNames.filter(name => get(PlayerName(name)).isDefined))
  }

  override def updateRanking(name: PlayerName, rankDelta: Int): Player = {
    val players = getPlayersFromDatabase
    val playerToUpdate = players.find(player => player.name.name.equals(name.name)).getOrElse(throw PlayerNotFoundException(name.name))
    val updatedRank = Elo(playerToUpdate.elo.rank + rankDelta)
    val updatedPlayer = Player(playerToUpdate.name, elo = updatedRank)
    val updatedPlayerList = updatedPlayer :: players.filter(player => !player.name.name.equals(playerToUpdate.name.name))
    Files.delete(StorageFilePath)
    updatedPlayerList.foreach(player => addPlayer(player))
    updatedPlayer
  }

  override def get(name: PlayerName): Option[Player] = {
    Try(getPlayersFromDatabase.filter(player => player.name.equals(name)).head).toOption
  }

  override def createPlayer(name: PlayerName): Player = {
    throwAnExceptionIfPlayerExists(name)
    val newPlayer = Player(name)
    addPlayer(newPlayer)
    newPlayer
  }

  private def createEmptyFileIfItNotExists() = {
    if(!Files.exists(StorageFilePath)) {
      Files.createFile(StorageFilePath)
    }
  }

  private def getPlayersFromDatabase: List[Player] = {
    val playersWithRanksStrings = readLinesFromFile
    val groupedPlayersData = playersWithRanksStrings.grouped(2).toList
    groupedPlayersData.map{playerData => Player(PlayerName(playerData.head), Elo(playerData(1).toInt))}
  }

  private def readLinesFromFile = {
    createEmptyFileIfItNotExists()
    io.Source.fromFile(StorageFilePath.toString).getLines.toList
  }

  private def toWriteFormat(player: Player): String = {
    player.name.name + "\n" + player.elo.rank + "\n"
  }

  private def write(content: String) = {
    createEmptyFileIfItNotExists()
    Files.write(Paths.get(storageFileName), content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND)
  }

  private def throwAnExceptionIfPlayerExists(playerName: PlayerName) = {
    if(get(playerName).isDefined) throw PlayerAlreadyExistsException(playerName.name)
  }

  //TODO walidacja pliku
}