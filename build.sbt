
import sbt.Keys._

lazy val root = (project in file(".")).
settings(
      name := "Raticado",
      version := "0.1",
      scalaVersion := "2.11.8",
      scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
      scalaSource in Test := baseDirectory.value / "src/test/unit/scala",
      libraryDependencies ++= {
      val akkaV = "2.3.6"
      val sprayV = "1.3.2"
      val Json4sVersion = "3.2.11"

      Seq(
      "io.spray"            %%  "spray-can"     % sprayV withSources() withJavadoc(),
      "io.spray"            %%  "spray-routing" % sprayV withSources() withJavadoc(),
      "io.spray"            %%  "spray-json"    % "1.3.1",
      "io.spray"            %%  "spray-servlet" % sprayV,
      "io.spray"            %%  "spray-testkit" % sprayV  % "test",
      "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
      "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
      "org.scalatest"       %%  "scalatest"     % "2.2.6" % "test",
      "net.codingwell"      %%  "scala-guice"   % "4.1.0",
      "org.mockito"          %  "mockito-core"  % "1.9.5",
      "com.typesafe.slick"  %%  "slick"         % "3.0.0",
      "com.typesafe.slick"  %%  "slick-codegen" % "3.0.0",
      "org.postgresql"       %  "postgresql"    % "9.4-1201-jdbc41",
      "com.zaxxer"           %  "HikariCP"      % "2.4.1",
      "org.json4s"          %%  "json4s-native" % Json4sVersion,
      "org.json4s"          %%  "json4s-ext"    % Json4sVersion
      )
    }
  ).
  enablePlugins(JavaAppPackaging)

tomcat()