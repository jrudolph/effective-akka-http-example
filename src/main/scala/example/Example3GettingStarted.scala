package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.io.StdIn
import scala.util.Failure
import scala.util.Success

object Example3GettingStarted extends App {
  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val materializer = ActorMaterializer()

  def handler(request: HttpRequest): Future[HttpResponse] =
    Future.successful(HttpResponse(entity = "Hello world"))

  Http().bindAndHandleAsync(handler, "localhost", 8080)
    .onComplete {
      case Success(_) ⇒
        println("Server started on port 8080. Type ENTER to terminate.")
        StdIn.readLine()
        system.terminate()
      case Failure(e) ⇒
        println("Binding failed.")
        e.printStackTrace
        system.terminate()
    }
}