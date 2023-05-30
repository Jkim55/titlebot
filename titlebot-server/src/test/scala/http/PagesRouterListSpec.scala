package http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import db.Page
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import http.PageRoutes
import io.circe.generic.auto._
import org.mockito.MockitoSugar.when
import org.scalatest.mockito.MockitoSugar.mock
import org.scalatest.{Matchers, WordSpec}
import service.PageQueryService
import util.ApiError

import java.util.UUID
import scala.concurrent.Future

class PagesRouterListSpec extends WordSpec with Matchers with ScalatestRouteTest {
  "http.PageRoutes" when {
    "GET /profiles" should {
      "return 200 and all Pages" in new Fixture {
        when(mockPageQueryService.getAllPages()).thenReturn(Future.successful(pages))

        Get("/pages") ~> pageRoutes ~> check {
          status shouldBe StatusCodes.OK
          val respPages = responseAs[Seq[Page]]
          respPages shouldBe pages
        }
      }
      "handle failure in the pages route" in new Fixture {
        when(mockPageQueryService.getAllPages()).thenReturn(Future.failed(new Exception()))
        Get("/pages") ~> pageRoutes ~> check {
          status shouldBe ApiError.generic.statusCode
        }
      }
    }

    "GET /profiles?pageUrl={pageUrl}" should {
      "return 200 and a models.Page" in new Fixture {
        when(mockPageQueryService.getPageByUrl(pageUrl)).thenReturn(Future.successful(testPage))

        Get(s"/pages?pageUrl=${pageUrl}") ~> pageRoutes ~> check {
          status shouldBe StatusCodes.OK
        }
      }
      "handle repository failure" in new Fixture {
        when(mockPageQueryService.getPageByUrl("some-page-url")).thenReturn(Future.failed(new Exception()))

        Get("/pages?pageUrl=some-page-url") ~> pageRoutes ~> check {
          status shouldBe ApiError.generic.statusCode
        }
      }
    }
  }
  trait Fixture {
    val mockPageQueryService = mock[PageQueryService]
    val pageRoutes = new PageRoutes(mockPageQueryService).routes

    val pageId = UUID.randomUUID().toString
    val pageUrl = "somewebpage.com"
    val testPage = Page(
      pageId,
      "somewebpage.com",
      "Some title for a webpage",
      "/fav/icon/location/favicon.ico"
    )
    val pages = Seq(testPage)
  }
}
