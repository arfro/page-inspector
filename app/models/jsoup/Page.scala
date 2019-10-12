package models.jsoup

import org.jsoup.nodes.Document


case class Page(rawInputLink: String, hostName: Option[String], doc: Document)

case class PageInspectionResult(htmlVersion: Option[String],
                                title: Option[String],
                                headings: List[Map[String, Int]],
                                links: Map[String, List[String]],
                                isLogin: Boolean)