package unsplash4s

import io.circe.Decoder
import io.circe.parser.{decode, _}
import sttp.client._
import sttp.client.asynchttpclient.future.AsyncHttpClientFutureBackend
import sttp.model.{StatusCode, Uri}
import unsplash4s.entities.U4sError._

import scala.concurrent.{ExecutionContext, Future}

class HttpClient(
  appConfig: UnsplashAppConfig,
  accessToken: Option[String] = None
)(implicit ec: ExecutionContext = ExecutionContext.global) {
  implicit val sttpBackend = AsyncHttpClientFutureBackend()

  def get[T: Decoder](uri: Uri): Future[T] = {
    basicRequest
      .get(uri)
      .headers(headers)
      .response(asStringAlways)
      .send()
      .map(jsonResponseToEntity[T])
  }

  def post[T: Decoder](uri: Uri, body: Option[String]): Future[T] = {
    basicRequest
      .post(uri)
      .body(body.getOrElse(""))
      .headers(headers)
      .response(asStringAlways)
      .send()
      .map(jsonResponseToEntity[T])
  }

  def delete[T: Decoder](uri: Uri): Future[T] = {
    basicRequest
      .delete(uri)
      .headers(headers)
      .response(asStringAlways)
      .send()
      .map(jsonResponseToEntity[T])
  }

  def apiGet[T: Decoder](path: String, query: Map[String, Any] = Map()): Future[T] = {
    get(uri"${appConfig.apiUrl + path}?$query")
  }

  def apiPost[T: Decoder](path: String, body: Option[String]): Future[T] = {
    post(uri"${appConfig.apiUrl + path}", body)
  }

  def apiDelete[T: Decoder](path: String, query: Map[String, Any] = Map()): Future[T] = {
    delete(uri"${appConfig.apiUrl + path}?$query")
  }

  def oauthPost[T: Decoder](path: String, body: Option[String]): Future[T] = {
    val url = appConfig.oauthUrl + path
    post(uri"$url", body)
  }

  private def headers: Map[String, String] = {
    Map(
      "Content-Type" -> "application/json",
      "Authorization" -> authorizationHeader,
      "Accept-Version" -> appConfig.apiVersion
    )
  }

  private def authorizationHeader: String = {
    accessToken match {
      case Some(token) => s"Bearer $token"
      case None => s"Client-ID ${appConfig.applicationAccessKey}"
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
