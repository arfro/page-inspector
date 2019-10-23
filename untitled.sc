

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

val afa = List(1,2,3,4,5,6,7)
  .grouped(4).toList
  .map(group => group.map(_ + 1))
