package config

import play.twirl.api.Html

object AppConfig {
  // normally this would be in application.conf
  val jsoupTimeout = 4000

  Html
}