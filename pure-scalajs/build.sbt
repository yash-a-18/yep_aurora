val scala3Version = "3.4.2"

enablePlugins(ScalaJSPlugin)

lazy val root = project
  .in(file("."))
  .settings(
    name := "pure-scalajs",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    scalaJSUseMainModuleInitializer := true,
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.8.0",
    // Depend on Laminar
    libraryDependencies += "com.raquo" %%% "laminar" % "17.0.0",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test
  )
