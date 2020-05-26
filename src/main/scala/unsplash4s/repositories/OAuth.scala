package unsplash4s.repositories

import io.circe.Json
import io.circe.syntax._
import sttp.client._
import unsplash4s.Decoders._
import unsplash4s.entities.AccessToken
import unsplash4s.entities.AccessToken.Scope
import unsplash4s.entities.AccessToken.Scope.Scope
import unsplash4s.{HttpClient, UnsplashAppConfig}

import scala.concurrent.Future

class OAuth(
  httpClient: HttpClient,
  appConfig: UnsplashAppConfig
) extends BaseRepository {
  def authorizationUrl(
    scope: Set[Scope] = Set(Scope.Public)
  ): String = {
    val query = Map(
      "client_id" -> appConfig.applicationAccessKey,
      "redirect_uri" -> appConfig.oauthRedirectUri,
      "response_type" -> "code",
      "scope" -> scope.mkString(" ")
    )
    val url = uri"${appConfig.oauthUrl}/authorize?$query"
    url.toString
  }

  def getAccessToken(code: String): Future[AccessToken] = {
    val body = Json.obj(
      "client_id" -> appConfig.applicationAccessKey.asJson,
      "client_secret" -> appConfig.applicationSecret.asJson,
      "redirect_uri" -> appConfig.oauthRedirectUri.asJson,
      "code" -> code.asJson,
      "grant_type" -> "authorization_code".asJson
    ).toString
    httpClient.oauthPost[AccessToken]("/token", Some(body))
  }
}
