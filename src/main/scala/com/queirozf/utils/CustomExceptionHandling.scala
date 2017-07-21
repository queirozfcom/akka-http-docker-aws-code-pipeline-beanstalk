package com.queirozf.utils

import java.time.format.DateTimeParseException

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import com.queirozf.utils.ResponseUtils._
import org.slf4j.LoggerFactory

import scala.util.control.NonFatal

/**
  * Created by felipe on 03/07/17.
  */
object CustomExceptionHandling {
  private val logger = LoggerFactory.getLogger("balance-tracker.errors")

  def handler = ExceptionHandler {

    case dpe: DateTimeParseException => complete(JsonError(dpe))
    case NonFatal(nf) => {
      logger.error("Non-fatal error thrown: ", nf)
      // avoid leaking information
      complete(JsonError(new Exception("An unexpected error occurred.")))
    }
    case t: Throwable => {
      logger.error("Fatal error thrown: ", t)

      complete(JsonError(new Exception("An unexpected error occurred.")))
    }
  }
}
