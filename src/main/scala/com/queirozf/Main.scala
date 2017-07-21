package com.queirozf

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.queirozf.routes.SampleRoutes
import com.queirozf.utils.{CustomExceptionHandling, CustomRejectionHandling}
import com.typesafe.config.ConfigFactory

/**
  * Created by felipe on 01/07/17.
  */
object Main extends App {

  implicit val system       = ActorSystem("main-actor-system")
  implicit val materializer = ActorMaterializer(ActorMaterializerSettings(system))
  implicit val ec           = system.dispatcher

  // this environment variable is set during deployment
  private val config = sys.env.get("CONF") match {
    case Some(confname) => ConfigFactory.load(confname)
    case None           => ConfigFactory.load()
  }

  val sampleRoutes = new SampleRoutes().routes

  val exceptionHandler = CustomExceptionHandling.handler
  val rejectionHandler = CustomRejectionHandling.handler

  val combinedRoutes =
    handleExceptions(exceptionHandler) {
      handleRejections(rejectionHandler) {
        {
          pathPrefix("v1") {
            sampleRoutes
          }
        }
      }
    }

  Http().bindAndHandle(combinedRoutes, "0.0.0.0", 5000)

}
