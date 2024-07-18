val scala3Version = "3.4.2"

enablePlugins(ScalaJSPlugin)

lazy val root = project
  .in(file("."))
  .settings(
    name := "hello-world",
    version := "0.1.0-SNAPSHOT",
    // This is an application with a main method
    scalaJSUseMainModuleInitializer := true,
    scalaVersion := scala3Version,
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.8.0",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test
  )
