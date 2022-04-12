package io.qhquanghuy.btcbillionaire.adapter.webservice

import akka.http.scaladsl.server.{Route, RejectionHandler}
import akka.http.scaladsl.model.{HttpResponse, HttpEntity, ContentTypes}
import akka.http.scaladsl.server.Directives._
import spray.json._

object AppRoute {
  implicit def rejectionHandler = RejectionHandler.default
    .mapRejectionResponse {
      case res @ HttpResponse(_, _, ent: HttpEntity.Strict, _) =>
        // since all Akka default rejection responses are Strict this will handle all rejections
        val message = ent.data.utf8String.replaceAll("\"", """\"""")

        // we copy the response in order to keep all headers and status code, wrapping the message as hand rolled JSON
        // you could the entity using your favourite marshalling library (e.g. spray json or anything else)
        res.withEntity(HttpEntity(ContentTypes.`application/json`, JsObject("message" -> JsString(message)).toString()))

      case x => x // pass through all other types of responses
    }


  def routes(routes: Route*): Route = Route.seal(concat(routes:_*))
}
