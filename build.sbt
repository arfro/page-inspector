name := """scout24-test"""
organization := "scout24"
version := "0.1"

scalaVersion := "2.13.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
val scalaTestPlusPlayVersion = "4.0.3"
val jsoupVersion = "1.7.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusPlayVersion % Test
libraryDependencies += "org.jsoup" % "jsoup" % jsoupVersion
