package service

import akka.http.scaladsl.testkit.ScalatestRouteTest
import db.{Page, PageRepository, PageRequest}
import org.mockito.MockitoSugar.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar.mock
import org.scalatest.{Matchers, WordSpec}

import java.util.UUID
import scala.concurrent.Future

class PageQueryServiceSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest {
  "service.PageQueryService" when {
    "getAll" should {
      "return all pages in repository" in new Fixture {
        when(mockPageRepository.all()).thenReturn(Future.successful(pages))
        val result = pageQueryService.getAllPages().futureValue
        result shouldBe pages
      }
    }
    "getPageByUrl" should {
      "return a page from repository if page exists in repository" in new Fixture {
        when(mockPageRepository.getPageByUrl(pageUrl)).thenReturn(Future.successful(Option(testPage1)))
        val result = pageQueryService.getPageByUrl(pageUrl).futureValue
        result shouldBe testPage1
      }
    }
  }

  trait Fixture {
    val mockPageRepository = mock[PageRepository]
    val pageRoutes = new PageQueryService(mockPageRepository)
    val pageQueryService = new PageQueryService(mockPageRepository)

    val pageId = UUID.randomUUID().toString
    val pageUrl = "somewebpage.com"

    val testPage1 = Page(
      pageId,
      "somewebpage.com",
      "Some title for a webpage",
      "/fav/icon/location/favicon.ico"
    )
    val testPage2 = Page(
      "another-random-uuid",
      "anotherwebpage.com",
      "Another title for a webpage",
      "http://location/favicon.ico"
    )
    val testPageRequest = PageRequest(
      "anotherwebpage.com",
      "Another title for a webpage",
      "http://location/favicon.ico"
    )
    val pages = Seq(testPage1, testPage2)
  }
}
