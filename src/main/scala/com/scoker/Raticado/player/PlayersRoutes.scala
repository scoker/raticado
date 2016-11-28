package com.scoker.Raticado.player

import com.google.inject.Inject
import com.scoker.Raticado.services.PlayerService
import spray.httpx.SprayJsonSupport._
import spray.routing.Route
import spray.routing.directives.RouteDirectives._
import spray.routing.directives.ExecutionDirectives._
import spray.routing.directives.MarshallingDirectives._
import spray.routing.directives.PathDirectives._
import spray.routing.directives.MethodDirectives._
import spray.routing.RouteConcatenation._

import scala.concurrent.ExecutionContext.Implicits

class PlayersRoutes @Inject() (playerService: PlayerService) {

  import PlayerJsonImplicits._

  private implicit val detachEc = Implicits.global
  private val PlayersPath = "players"

  def routes: Route = {
    pathPrefix(PlayersPath) {
      pathEnd {
        getPlayers ~
        addPlayer()
      }

    }
  }

  private def getPlayers: Route = {
    get {
      complete {
        playerService.listPlayers()
      }
    }
  }

  private def addPlayer(): Route = {
    post {
      detach() {
        import PlayerNameJsonImplicits._
        entity(as[PlayerName]){ name =>
          complete {
            playerService.createPlayer(name)
          }
        }
      }
    }
  }
}
