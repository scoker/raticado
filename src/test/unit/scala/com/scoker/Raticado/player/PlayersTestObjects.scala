package com.scoker.Raticado.player

trait PlayersTestObjects {
  val Krzychu = Player(PlayerName("Krzychu"))
  val Andriu = Player(PlayerName("Andriu"))
  val Zgredek = Player(PlayerName("Zgredek"))
  val Claudia = Player(PlayerName("Claudia"))
  val AnyPlayer = Player(PlayerName("Some Name"))

  val teamOfFour = Team("4 mambers team", Set("Krzychu", "Andriu", "Zgredek", "Claudia"))
  val KrzychuZgredakTeam = Team("2 mambers team", Set("Krzychu", "Zgredek"))
}
