package com.queirozf.routes

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.queirozf.utils.ResponseUtils._

/**
  * @param system
  */
class SampleRoutes(implicit val system: ActorSystem) {

  import system.dispatcher
  implicit val timeout = Timeout(32, TimeUnit.MILLISECONDS)

  // sample route: localhost:5000/v1/999/test
  def routes = {
    pathPrefix(LongNumber) { param =>
      path("test") {
        get {
          getTest(param)
        }
      }
    }
  }

  private def getTest(param: Long): Route = {
    complete {
      JsonOk(param.toString)
    }
  }
}
