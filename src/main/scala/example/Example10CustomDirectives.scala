package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpCharsets
import akka.http.scaladsl.model.MediaType
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.io.StdIn
import scala.util.Failure
import scala.util.Success

object Example10CustomDirectives extends App {
  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val materializer = ActorMaterializer()

  import akka.http.scaladsl.server.Directives._

  val route =
    path("user") {
      headerValueByName("X-Request-Id") { id ⇒ // propagate header to response
        respondWithHeader(RawHeader("X-Request-Id", id)) {
          complete(s"Request with ID: $id")
        }
      }
    }

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
/*





















  val propagateRequestId =
    headerValueByName("X-Request-Id").flatMap { id ⇒
      respondWithHeader(RawHeader("X-Request-Id", id)) & provide(id)
    }


 */ 