package models.errors

trait Problem
case class HtmlExtractionError(msg: String) extends Problem
