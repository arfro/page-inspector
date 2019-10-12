package errors.jsoup

trait JSoupError
case class HtmlExtractionError(msg: String) extends JSoupError
