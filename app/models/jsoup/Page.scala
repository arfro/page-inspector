package models.jsoup

import org.jsoup.nodes.Document


case class Page(rawInputLink: String, hostName: Option[String], doc: Document)
