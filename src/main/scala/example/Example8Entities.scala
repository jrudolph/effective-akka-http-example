package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString

import scala.io.StdIn
import scala.util.Failure
import scala.util.Success

object Example8Entities extends App {
  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val materializer = ActorMaterializer()

  import akka.http.scaladsl.server.Directives._

  val route =
    get {
      path("user") {
        complete {
          "test"
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
HttpEntity.Strict(ContentTypes.`text/plain(UTF-8)`, ByteString("test"))
HttpEntity.Default(ContentTypes.`text/plain(UTF-8)`, 4, Source.single(ByteString("te")) ++ Source.single(ByteString("st")))

{
  val entity = HttpEntity.CloseDelimited(ContentTypes.`text/plain(UTF-8)`, Source.single(ByteString("te")) ++ Source.single(ByteString("st")))
  HttpResponse(entity = entity)
}

{
  val entity = HttpEntity.Chunked.fromData(ContentTypes.`text/plain(UTF-8)`, Source.single(ByteString("te")) ++ Source.single(ByteString("st")))
  HttpResponse(entity = entity)
}

*/
