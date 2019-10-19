package services.inspector.jsoup

import config.AppConfig
import org.jsoup.{Connection, Jsoup}

class JSoupWrapper {

  def connect(link: String): Connection = Jsoup
    .connect(link)
    .timeout(AppConfig.jsoupTimeout)

}
