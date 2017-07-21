package routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.queirozf.routes.SampleRoutes
import com.queirozf.utils.{CustomExceptionHandling, CustomRejectionHandling}
import org.scalatest.{Matchers, WordSpec}

class SampleRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest {

  implicit val exceptionHandler = CustomExceptionHandling.handler
  implicit val rejectionHandler = CustomRejectionHandling.handler

  val sampleRoutes = new SampleRoutes().routes

  "sampleRoutes" can {

    "test" should {

      "respond 200 with correct payload for test route" in {

        // see http://doc.akka.io/docs/akka-http/current/scala/http/routing-dsl/testkit.html#testing-sealed-routes
        Get(s"/999/test") ~> Route.seal(sampleRoutes) ~> check {
          status shouldEqual StatusCodes.OK
          responseAs[String] shouldEqual "999"
        }

      }
    }
  }
}
