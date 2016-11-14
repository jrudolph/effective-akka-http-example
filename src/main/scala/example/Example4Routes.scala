package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.io.StdIn
import scala.util.Failure
import scala.util.Success

object Example4Routes extends App {
  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val materializer = ActorMaterializer()

  import akka.http.scaladsl.server.Directives._

  // TODO: make this a route instead
  val userRoute = path("user" / IntNumber) { id ⇒
    complete(s"Hello world with id $id")
  }

  val route: Route =
    get {
      path("another") {
        parameters("name", "age".as[Int].?) { (name, age: Option[Int]) ⇒
          val ageIn10Years = age.map(_ + 10).getOrElse(999)
          complete(s"Hello $name. You are $ageIn10Years in ten years")
        } ~
          parameter("age") { age ⇒
            respondWithHeader(headers.`Last-Modified`(DateTime.now)) {
              respondWithHeader(headers.`Last-Modified`(DateTime.now)) {
                complete(s"Only age $age")
              }
            }
          }
      }
    }
  /*def handler(request: HttpRequest): Future[HttpResponse] = request match {
    case HttpRequest(HttpMethods.GET, Uri.Path("/abc"), _, _, _) ⇒
      Future.successful(HttpResponse(entity = "Hello world"))
  }*/

  Http().bindAndHandleAsync(Route.asyncHandler(route), "localhost", 8080)
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
