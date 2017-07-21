package com.queirozf.utils
import akka.http.scaladsl.model._
import org.json4s.JsonAST.{JNull, JObject, JString}
import org.json4s.jackson.JsonMethods._

import scala.util.control.NonFatal

/**
  * Created by felipe on 02/07/17.
  */
object ResponseUtils {

  def JsonOk(body: String): HttpResponse =
    HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentType(MediaTypes.`application/json`), body))

  def JsonOk: HttpResponse = JsonOk("")

  def JsonBadRequest(attrName: String, errorMsg: String, maybeThrowable: Option[Throwable]): HttpResponse =
    maybeThrowable match {
      case Some(thr) =>
        thr match {
          case NonFatal(nf) =>
            HttpResponse(
              StatusCodes.BadRequest,
              entity = HttpEntity(
                ContentType(MediaTypes.`application/json`),
                pretty(
                  JObject(
                    attrName -> JObject(
                      "error"      -> JString(errorMsg),
                      "class"      -> JString(nf.getClass.toString),
                      "message"    -> JString(nf.getMessage),
                      "underlying" -> (if (nf.getCause == null) JNull else JString(nf.getCause.getMessage))
                    )
                  )
                )
              )
            )
          case fatal => throw fatal
        }
      case None => JsonBadRequest(attrName, errorMsg)
    }

  def JsonBadRequest(attrName: String, e: Throwable): HttpResponse = JsonBadRequest(attrName, errorMsg = "", Some(e))

  def JsonBadRequest(attrName: String, errorMsg: String): HttpResponse = JsonBadRequest(attrName, errorMsg, None)

  def JsonBadRequest(e: Throwable): HttpResponse = JsonBadRequest(attrName = "", errorMsg = "", Some(e))

  def JsonError(e: Throwable): HttpResponse = e match {
    case NonFatal(nf) =>
      HttpResponse(
        StatusCodes.InternalServerError,
        entity = HttpEntity(
          ContentType(MediaTypes.`application/json`),
          pretty(
            JObject(
              "class"      -> JString(nf.getClass.toString),
              "message"    -> JString(nf.getMessage),
              "stackTrace" -> JString(nf.getStackTrace.map(el => el.toString).mkString("\n"))
            )
          )
        )
      )
    case fatal => throw fatal
  }

  def JsonNotFound: HttpResponse =
    HttpResponse(StatusCodes.NotFound, entity = HttpEntity(ContentType(MediaTypes.`application/json`), ""))

}
