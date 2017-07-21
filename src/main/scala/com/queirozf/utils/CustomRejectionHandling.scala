package com.queirozf.utils

import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.{MalformedQueryParamRejection, MalformedRequestContentRejection, RejectionHandler}
import com.queirozf.utils.ResponseUtils.JsonBadRequest

/**
  * Created by felipe on 03/07/17.
  */
object CustomRejectionHandling {

  /**
    *
    * @return
    */
  def handler = {
    RejectionHandler
      .newBuilder()
      .handle {
        case MalformedRequestContentRejection(msg, e) => complete(JsonBadRequest("GLOBAL", msg, Some(e)))
        case MalformedQueryParamRejection(parameterName, errorMsg, maybeThrowable) =>
          maybeThrowable match {
            case Some(e) => complete(JsonBadRequest(parameterName,errorMsg, Some(e)))
            case None    => complete(JsonBadRequest(parameterName, errorMsg))
          }
      }
      .result()
      .withFallback(RejectionHandler.default)
  }
}
