import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import http.Router

import scala.concurrent.{ExecutionContext, Future}

class Server(router: Router, host: String, port: Int)(implicit system: ActorSystem, ec: ExecutionContext, mat: ActorMaterializer) {
  def bind(): Future[ServerBinding] = Http().bindAndHandle(router.routes, host, port)
}
