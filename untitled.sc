

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

ws.url(url).withRequestTimeout(10000.millis).get()