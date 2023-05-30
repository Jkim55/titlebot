package http

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.{Directives, Route}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import service.PageQueryService
import util.PageDirectives

import scala.concurrent.ExecutionContext

trait Router {
  def routes: Route
}

class PageRoutes(pageQueryService: PageQueryService)(implicit executionContext: ExecutionContext) extends Router with Directives with PageDirectives {
  override def routes: Route = cors() {
    pathPrefix("pages") {
      pathEndOrSingleSlash {
        get {
          parameters("pageUrl") { pageUrl =>
            handleWithGeneric(pageQueryService.getPageByUrl(pageUrl)) { page =>
              complete(ToResponseMarshallable(page))
            }
          }
        } ~ get {
          handleWithGeneric(pageQueryService.getAllPages()) { pages =>
            complete(ToResponseMarshallable(pages))
          }
        }
      }
    }
  }
}
