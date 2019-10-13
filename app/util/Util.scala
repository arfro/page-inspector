package util

import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

object Util {

  def sanitizeInput(input: String) = {
    val trimmedAndWithoutWww = input.trim.replace("www.", "")
    Jsoup.clean(trimmedAndWithoutWww, Whitelist.basic())
  }

}
