package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.io.StdIn
import scala.util.Failure
import scala.util.Success

object Example5PathSetup extends App {
  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val materializer = ActorMaterializer()

  import akka.http.scaladsl.server.Directives._

  val route =
    get {
      path("user" / "def") {
        complete("Hello user")
      } ~
        path("user" / "ghi") {
          complete("Hello ghi")
        } ~
        path("user" / Segment) { other ⇒
          complete(s"Hello '$other'")
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
