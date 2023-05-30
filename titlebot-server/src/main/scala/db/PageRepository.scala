package db

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

trait PageRepository {
  def all(): Future[Seq[Page]]

  def getPageByUrl(pageUrl: String): Future[Option[Page]]

  def createPage(pageRequest: PageRequest): Future[Page]
}

class InMemoryPageRepository(initialPages: Seq[Page] = Seq.empty)(implicit ec: ExecutionContext) extends PageRepository {
  private var pages: Vector[Page] = initialPages.toVector

  override def all(): Future[Seq[Page]] = Future.successful(pages)

  override def getPageByUrl(pageUrl: String): Future[Option[Page]] = {
    val pageResults = pages.find(_.url == pageUrl)
    Future.successful(pageResults)
  }

  override def createPage(pageRequest: PageRequest): Future[Page] = Future.successful {
    val page = Page(
      UUID.randomUUID().toString,
      pageRequest.url,
      pageRequest.title,
      pageRequest.favIconUrl
    )
    pages = pages :+ page
    page
  }
}
