package models

import org.jsoup.nodes.Document


case class Page(rawInputLink: String, hostName: Option[String], doc: Document)

case class PageInspectionResult(htmlVersion: String,
                                title: String,
                                headings: Map[String, Int],
                                links: Map[String, List[String]],
                                isLogin: Boolean)