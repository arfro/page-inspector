package services.linkvalidation


import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import config.AppConfig
import javax.inject.Inject
import play.api.libs.ws._

import scala.concurrent.Future


trait ValidationResult
case class ValidationSuccess(url: String, status: Int) extends ValidationResult
case class ValidationFailure(url: String, problem: String) extends ValidationResult

class LinkValidationService @Inject() (ws: WSClient)  {

  // TODO: inject execution context

  def validate(listOfUrls: List[String]): Future[List[ValidationResult]] = batchProcess(listOfUrls, AppConfig.urlsPerBatch)

  // 1. divide links into batches
  // 2. wrap each batch in future for parallel execution
  // 3. for each batch send a request and get a response and then wrap it in ValidationResult case class
  // 4. add batch results to result accumulator for foldLeft - that at the end will return one Future[List[ValidationResult]]
  //

  // TODO: fix naming of those functions
  private def batchProcess(listOfUrls: List[String], urlsPerBatch: Int = 50): Future[List[ValidationResult]] = {
    // divide links into batches, amount of links per batch is configurable via config with a default of 50
    val batches = listOfUrls.grouped(urlsPerBatch).toList
    println(s"Processing ${batches.length} batch(es)")

    // i want to wrap each batch in a Future to execute in parallel, using foldLeft because i want to have a different return type than 'batches' had
    // "on the left" we will have Future[List[ValidationResult]] and "on the right" List[String]
    batches.foldLeft(Future.successful(List.empty[ValidationResult]))(processBatch)

  }

  // as per foldLeft this function operates on "left" side which is already of type "Future[List[ValidationResult]] and "right" which is still of "old" type
  // to be able to fold the result type of this functoin obvs needs to be of the same return type as the "new" type
  private def processBatch(responsesFoldAccumulator: Future[List[ValidationResult]], batch: List[String]): Future[List[ValidationResult]] = {

    responsesFoldAccumulator.flatMap { responses =>
      val batchFutures: List[Future[ValidationResult]] = batch.map(url => {
        (for {
            response <- {
              println(s"About to send req for $url")
              ws.url(url).withRequestTimeout(2000.millis).get()
            }
            _ = println(s"Response for $url received")
            result = ValidationSuccess(url, response.status)
         } yield result)
          .recover{case e: Exception => ValidationFailure(url, e.getMessage)}
      })

      // generating one big Future out of all sequences of Futures so I can map below and add to the accumulator
      val batchFuture: Future[List[ValidationResult]] = Future.sequence(batchFutures)

      batchFuture.map { batchResponses =>
        println("Batch processed")
        responses ++ batchResponses
      }
    }
  }


}
