package com.scoker.Raticado.matches

import com.google.inject.Inject
import com.scoker.Raticado.services.MatchService
import spray.routing.Route
import spray.routing.directives.ExecutionDirectives._
import spray.routing.directives.MarshallingDirectives._
import spray.routing.directives.MethodDirectives._
import spray.routing.directives.PathDirectives._
import spray.routing.directives.RouteDirectives._
import spray.httpx.SprayJsonSupport._
import spray.routing.RouteConcatenation._

import scala.concurrent.ExecutionContext.Implicits


class MatchesRoutes@Inject() (matchService: MatchService) {
  import MatchJsonImplicits._
  import FixtureJsonImplicits._

  private implicit val detachEc = Implicits.global


  val routes: Route = {
    pathPrefix("matches") {
      pathEnd {
        addMatch()
      }
    } ~
    pathPrefix("fixtures") {
      pathEnd {
        addAllMatches()
      }
    }
  }

  private def addMatch(): Route = {
    post {
      detach() {
        entity(as[Match]){ aMatch =>
          complete {
            matchService.addMatch(aMatch)
            "Done"
          }
        }
      }
    }
  }

  private def addAllMatches(): Route = {
    post{
      detach() {
        entity(as[Fixture]){ fixture =>
          complete {
            matchService.addAllMatches(fixture)
            "Done"
          }
        }
      }
    }
  }
}
