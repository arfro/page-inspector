name := """scout24-test"""
organization := "scout24"
version := "0.1"

scalaVersion := "2.12.8"

val scalaTestPlusPlayVersion = "4.0.3"
val jsoupVersion = "1.7.2"
val scalaTestVersion = "3.0.8"
val mockitoVersion = "3.0.0"

libraryDependencies += guice
libraryDependencies += "org.jsoup" % "jsoup" % jsoupVersion
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusPlayVersion % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
libraryDependencies += "org.mockito" % "mockito-core" % mockitoVersion % "test"

lazy val root = (project in file(".")).enablePlugins(PlayScala)