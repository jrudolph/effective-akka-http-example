package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpCharset
import akka.http.scaladsl.model.HttpCharsets
import akka.http.scaladsl.model.MediaType
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import spray.json.JsValue

import scala.io.StdIn
import scala.util.Failure
import scala.util.Success

object Example9MarshallingCustomTypes extends App {
  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val materializer = ActorMaterializer()

  import akka.http.scaladsl.server.Directives._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

  case class Person(name: String, age: Int)
  object Person {
    import spray.json.DefaultJsonProtocol._

    implicit val personFormat = jsonFormat2(Person.apply) // or use spray-json-shapeless
  }

  val customMediaType = MediaType.customWithFixedCharset("application", "x-my-json", HttpCharsets.`UTF-8`)

  val route =
    get {
      path("user") {
        complete(Person("Ann", 35))
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

import SprayJsonSupport.sprayJsonMarshaller

implicit val customJsonMarshaller =
    SprayJsonSupport.sprayJsonMarshaller[Person]
      .wrap(customMediaType)((json: Person) ⇒ json)
 */ 