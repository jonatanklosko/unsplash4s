package unsplash4s

import sttp.client.asynchttpclient.future.AsyncHttpClientFutureBackend
import sttp.client._
import io.circe.parser.decode
import io.circe.parser._
import io.circe.Decoder
import sttp.model.{StatusCode, Uri}
import unsplash4s.entities.U4sError.{ForbiddenError, JsonParsingError, NotFoundError, UnauthorizedError, UnhandledResponseError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Client {
  val DefaultApiUrl = "https://api.unsplash.com"
  val DefaultOAuthUrl = "https://unsplash.com/oauth"
  val ApiVersion = "v1"
}

class Client(
  val applicationAccessKey: String,
  val apiUrl: String = Client.DefaultApiUrl,
  val oauthUrl: String = Client.DefaultOAuthUrl,
  val applicationSecret: Option[String] = None,
  val oauthRedirectUri: Option[String] = None,
  val accessToken: Option[String] = None
) {
  implicit val backend = AsyncHttpClientFutureBackend()

  def request[T: Decoder](path: String, query: Map[String, Any] = Map()): Future[T] = {
    val url = apiUrl + path
    basicRequest
      .get(uri"$url?$query")
      .contentType("application/json")
      .header("Authorization", authorizationHeader)
      .header("Accept-Version", Client.ApiVersion)
      .response(asStringAlways)
      .send()
      .map(jsonResponseToEntity[T])
  }

  def postRequest[T: Decoder](url: Uri, body: String): Future[T] = {
    basicRequest
      .post(url)
      .body(body)
      .contentType("application/json")
      .header("Authorization", authorizationHeader)
      .header("Accept-Version", Client.ApiVersion)
      .response(asStringAlways)
      .send()
      .map(jsonResponseToEntity[T])
  }

  private def authorizationHeader: String = {
    accessToken match {
      case Some(token) => s"Bearer $token"
      case None => s"Client-ID $applicationAccessKey"
    }
  }

  private def jsonResponseToEntity[T: Decoder](response: Response[String]): T = {
    if (response.isSuccess) {
      decode[T](response.body) match {
        case Left(error) => throw JsonParsingError(error.getMessage, response.body, Some(error))
        case Right(entity) => entity
      }
    } else {
      val message = parse(response.body) match {
        case Left(_) => "Something went wrong."
        case Right(json) => json.hcursor.downField("errors").as[List[String]].getOrElse(List()).mkString(" ")
      }
      response.code match {
        case StatusCode.Unauthorized => throw UnauthorizedError(message)
        case StatusCode.Forbidden => throw ForbiddenError(message)
        case StatusCode.NotFound => throw NotFoundError(message)
        case _ => throw UnhandledResponseError(message, response.body)
      }
    }
  }
}
