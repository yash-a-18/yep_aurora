val scala3Version = "3.4.2"
val zioVersion = "2.0.15"

lazy val root = project
  .in(file("."))
  .settings(
    name := "zio-microservices",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    libraryDependencies += "dev.zio" %% "zio" % zioVersion,
    libraryDependencies += "dev.zio" %% "zio-json" % "0.6.0",
    libraryDependencies += "dev.zio" %% "zio-http" % "3.0.0-RC2",
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test
  )

  //MArshalling
  //Effectful Programming, Side Effects
  // %%% scaala js
  // %% Scala
  // % jar files

  // ZLayer - alternative for configs