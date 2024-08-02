val scala3Version = "3.4.2"
val zioVersion = "2.0.15"

lazy val root = project
  .in(file("."))
  .settings(
    name := "user-server",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "dev.zio" %% "zio" % zioVersion,
    libraryDependencies += "dev.zio" %% "zio-json" % "0.6.0",
    libraryDependencies += "dev.zio" %% "zio-http" % "3.0.0-RC2",
    // https://mvnrepository.com/artifact/io.getquill/quill-jdbc-zio
    libraryDependencies += "io.getquill" %% "quill-jdbc-zio" % "4.8.5",
    // https://mvnrepository.com/artifact/io.getquill/quill-jdbc
    libraryDependencies += "io.getquill" %% "quill-jdbc" % "4.8.5",
    // https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
    libraryDependencies += "com.mysql" % "mysql-connector-j" % "9.0.0", // It was at mysql-connector-java but is now moved to mysql-connector-j
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test,
    // https://mvnrepository.com/artifact/dev.zio/zio-test
    libraryDependencies += "dev.zio" %% "zio-test" % "2.1.6" % Test,
    // https://mvnrepository.com/artifact/dev.zio/zio-test-sbt
    libraryDependencies += "dev.zio" %% "zio-test-sbt" % "2.1.6" % Test
  )

//MArshalling
//Effectful Programming, Side Effects
// %%% scala js
// %% Scala
// % jar files

// ZLayer - alternative for configs
