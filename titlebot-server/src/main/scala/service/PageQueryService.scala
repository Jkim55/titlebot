package service

import db.{Page, PageRepository, PageRequest}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.concurrent.{ExecutionContext, Future}

class PageQueryService(private val pageRepository: PageRepository)(implicit executionContext: ExecutionContext) {
  def getAllPages(): Future[Seq[Page]] = pageRepository.all()

  def cleanUpPageUrl(userSubmittedUrl: String): String = {
    if (userSubmittedUrl.endsWith("/")) (userSubmittedUrl.take(userSubmittedUrl.length - 1)) else userSubmittedUrl
  }

  def getPageByUrl(pageUrl: String): Future[Page] = {
    val cleanUrl = cleanUpPageUrl(pageUrl)
    val existingPage = pageRepository.getPageByUrl(cleanUrl)

    existingPage flatMap {
      case Some(pageDetails) => Future.successful(pageDetails)
      case None => save(cleanUrl)
    }
  }

  private def save(pageUrl: String): Future[Page] = {
    val pageRequest = transformToPageRequest(pageUrl)
    pageRepository.createPage(pageRequest)
  }

  def transformToPageRequest(pageUrl: String) = {
    val htmlDoc: Document = getHtmlDoc(pageUrl)
    val title = htmlDoc.title()
    val favIconUrl = formFavIcon(pageUrl, extractFavIconUrl(htmlDoc))

    PageRequest(
      pageUrl,
      title,
      favIconUrl
    )
  }

  def getFavIconUrl(pageUrl: String, favIconUrl: String): String = {
    if (favIconUrl.contains("https://") || favIconUrl.isBlank) favIconUrl else formatFavIconUrl(pageUrl, favIconUrl)
  }

  def formFavIcon(pageUrl: String, favIconUrl: String): String = {
    ((favIconUrl.isBlank || favIconUrl.contains("https://")), favIconUrl(0) != '/') match {
      case (false, true) => formatFavIconUrl(pageUrl, prefixRelativePath(favIconUrl))
      case (false, false) => formatFavIconUrl(pageUrl, favIconUrl)
      case _ => favIconUrl
    }
  }

  private def prefixRelativePath (favIconUrl: String) = s"/${favIconUrl}"

  private def formatFavIconUrl(pageUrl: String, relativePath: String): String = {
    val trimmedPageUrl = pageUrl.replaceAll("""([^.]*?com).*""", "com")
    s"https://${trimmedPageUrl}${relativePath}"
  }

  private def extractFavIconUrl(htmlDoc: Document) = {
    val matchedLinkElements = htmlDoc.head.select("link[href*=ico]")
    if (matchedLinkElements.isEmpty) "" else matchedLinkElements.first.attr("href")
  }

  private def getHtmlDoc(pageUrl: String) = {
    val formattedUrl = s"http://${pageUrl}"
    Jsoup.connect(formattedUrl)
      .userAgent("Chrome")
      .get()
  }
}
