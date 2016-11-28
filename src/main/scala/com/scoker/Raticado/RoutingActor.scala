package com.scoker.Raticado

import akka.actor.Actor
import com.google.inject.Inject
import com.scoker.Raticado.matches.MatchesRoutes
import com.scoker.Raticado.player.PlayersRoutes
import com.scoker.Raticado.services.ScoringService
import spray.routing.HttpService

class RoutingActor extends Actor with HttpService with RouteHandlers {
  @Inject private var playersRoutes: PlayersRoutes = _
  @Inject private var matchesRoutes: MatchesRoutes = _

  override def receive = runRoute(playersRoutes.routes ~ matchesRoutes.routes)

  override def actorRefFactory = context
}
