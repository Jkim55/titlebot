package util

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class PageDirectivesSpec extends WordSpec with Matchers with ScalatestRouteTest with Directives with PageDirectives {
  private val testRoute = pathPrefix("test") {
    path("success") {
      get {
        handleWithGeneric(Future.unit) { _ =>
          complete(StatusCodes.OK)
        }
      }
    } ~ path("failure") {
      get {
        handleWithGeneric(Future.failed(new Exception("Failed future returned"))) { _ =>
          complete(StatusCodes.OK)
        }
      }
    }
  }

  "util.PageDirectives" should {
    "not return an error if the future succeeds" in {
      Get("/test/success") ~> testRoute ~> check {
        status shouldBe StatusCodes.OK
      }
    }

    "return an error if the future fails" in {
      Get("/test/failure") ~> testRoute ~> check {
        status shouldBe StatusCodes.InternalServerError
      }
    }
  }
}
