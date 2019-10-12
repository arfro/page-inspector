import org.jsoup.Jsoup
import scala.util.Try
import scala.util.{Success, Failure}
import org.jsoup.nodes.DocumentType
import org.jsoup.select.Elements
import org.jsoup.select.NodeVisitor
import org.jsoup.nodes.Element

def extractHtml(link: String) =
  Try(Jsoup
    .connect(link)
    .timeout(34224)
    .get()
  )

val doc = extractHtml("http://github.com").get

var allLinks: List[String] = List()

doc.getElementsByTag("a")
  .stream()
  .forEach(elem => {
    allLinks = elem.attr("href") :: allLinks
  })

allLinks.partition(s => s.startsWith("/") || s.startsWith("#"))

//.forEach(link => println(link.attr("href")))