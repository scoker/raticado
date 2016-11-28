package com.scoker.Raticado

import java.io.{ByteArrayOutputStream, PrintStream}

import org.json4s.MappingException
import spray.http.StatusCodes._
import spray.http.{HttpResponse, IllegalRequestException, RequestProcessingException, StatusCode}
import spray.routing._
import spray.routing.directives.RouteDirectives.complete

import scala.util.control.Exception._
import scala.util.control.NonFatal

trait RouteHandlers {
  implicit val rejectionHandler = {
    RejectionHandler {
      case Nil => complete(HttpResponse(NotFound))
      case MethodRejection(_) :: _ => complete(HttpResponse(BadRequest))
      case UnsupportedRequestContentTypeRejection(_) :: _ =>
        returnErrorMessage(BadRequest, "Request is expected to have 'application/json' content type")
      case MalformedRequestContentRejection(_, _) :: _ =>
        returnErrorMessage(BadRequest, "The request content was malformed")
      case RequestEntityExpectedRejection :: _ =>
        returnErrorMessage(BadRequest, "Request is expected to have json entity supplied")
      case MissingHeaderRejection(fieldName) :: _ =>
        returnErrorMessage(BadRequest, s"Request is missing required HTTP header `$fieldName`")
    }
  }

  implicit val exceptionHandler = {
    ExceptionHandler {
      case NonFatal(exception) =>
        returnErrorMessage(InternalServerError, exception.getMessage)
    }
  }

  private def returnErrorMessage(statusCode: StatusCode, message: String): StandardRoute = {
    complete((statusCode, message))
  }
}

