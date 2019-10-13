package unit.util

object Util {

  def sanitizeInput(input: String) = input.trim.replace("www.", "")


}
