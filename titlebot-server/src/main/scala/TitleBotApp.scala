import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import http.PageRoutes
import db.InMemoryPageRepository
import service.PageQueryService

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object TitleBotApp extends App {

  private val host = "0.0.0.0"
  private val port = 9000

  implicit val system: ActorSystem = ActorSystem(name = "page-api")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  import system.dispatcher

  private val pageRepository = new InMemoryPageRepository(Seq())
  private val pageQueryService = new PageQueryService(pageRepository)
  private val router = new PageRoutes(pageQueryService)
  private val server = new Server(router, host, port)

  private val binding = server.bind()
  binding.onComplete {
    case Success(_) => println(s"Server running on http://${host}:${port}")
    case Failure(error) => println(s"Failed to start up server: ${error.getMessage}")
  }

  Await.result(binding, 3.seconds)
}
