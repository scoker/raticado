package com.scoker.Raticado.calculator

import spray.json.DefaultJsonProtocol

case class Elo(rank: Int)

object EloJsonImplicits extends DefaultJsonProtocol {
  implicit val eloFormat = jsonFormat1(Elo)
}