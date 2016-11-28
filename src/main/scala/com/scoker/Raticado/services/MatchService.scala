package com.scoker.Raticado.services

import com.scoker.Raticado.matches.{Fixture, Match}

trait MatchService {

  def addMatch(aMatch: Match): Unit
  def addAllMatches(fixture: Fixture): Unit
  def listMatches(): List[Match]
}
