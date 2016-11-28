package com.scoker.Raticado.player

import com.scoker.Raticado.RouteUnitTests
import com.scoker.Raticado.services.PlayerService
import spray.http.{ContentTypes, HttpEntity, StatusCodes}
import spray.httpx.SprayJsonSupport._
import org.mockito.Mockito._

class PlayersRoutesTest extends RouteUnitTests {

  private var tested: PlayersRoutes = _
  private var mockedPlayerService: PlayerService = _

  before {
    mockedPlayerService = mock[PlayerService]
    tested = new PlayersRoutes(mockedPlayerService)
    when(mockedPlayerService.listPlayers()).thenReturn(Set[Player]())
  }

  "/players GET" should "return empty set if no players are in system" in {
    Get("/players") ~> tested.routes ~> check {
      import PlayerJsonImplicits._
      status shouldEqual StatusCodes.OK
      responseAs[Set[Player]] shouldEqual Set[Player]()
    }
  }

  it should "return one player if only one player is in system" in {
    when(mockedPlayerService.listPlayers()).thenReturn(Set(Krzychu))
    Get("/players") ~> tested.routes ~> check {
      import PlayerJsonImplicits._
      status shouldEqual StatusCodes.OK
      responseAs[Set[Player]] shouldEqual Set(Krzychu)
    }
  }

  "/players POST" should "call PlayerService to create player" in {
    when(mockedPlayerService.createPlayer(Krzychu.name)).thenReturn(Krzychu)
    Post("/players", HttpEntity(ContentTypes.`application/json`, KrzychuJson)) ~> tested.routes ~> check {
      status shouldEqual StatusCodes.OK
      verify(mockedPlayerService, times(1)).createPlayer(Krzychu.name)
    }
  }

  it should "create player without specified elo ranking" in {
    when(mockedPlayerService.createPlayer(Krzychu.name)).thenReturn(Krzychu)
    Post("/players", HttpEntity(ContentTypes.`application/json`, noEloKrzychuJson)) ~> tested.routes ~> check {
      status shouldEqual StatusCodes.OK
      verify(mockedPlayerService, times(1)).createPlayer(Krzychu.name)
    }
  }

  private val Krzychu = Player(PlayerName("Krzychu"))
  private val KrzychuJson =
    """{
      |  "name": "Krzychu",
      |  "elo": {
      |    "rank": 1500
      |    }
      |  }""".stripMargin
  private val noEloKrzychuJson =
    """{
      |  "name": "Krzychu"
      |  }""".stripMargin
}