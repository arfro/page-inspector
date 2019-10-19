package unit.util

import config.AppConfig
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object Util {

  def sanitizeInput(input: String) = input.trim.replace("www.", "")

}

object JSoupUtil {
  def jSoupConnectAndGetHtmlFctn(link: String): Document = {
    Jsoup
      .connect(link)
      .timeout(AppConfig.jsoupTimeout)
      .get()
  }
}
