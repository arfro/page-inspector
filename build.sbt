name := """scout24-test"""
organization := "scout24"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
libraryDependencies += "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.4"
libraryDependencies += "org.jsoup" % "jsoup" % "1.7.2"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "scout24.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "scout24.binders._"
