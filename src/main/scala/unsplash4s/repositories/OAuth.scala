package unsplash4s.repositories

import io.circe.syntax._
import sttp.client._
import unsplash4s.{HttpClient, UnsplashAppConfig}
import unsplash4s.entities.AccessToken
import unsplash4s.Decoders._
import unsplash4s.entities.AccessToken.Scope
import unsplash4s.entities.AccessToken.Scope.Scope

import scala.concurrent.Future

class OAuth(
  httpClient: HttpClient,
  appConfig: UnsplashAppConfig
) {
  def authorizationUrl(
    scope: Seq[Scope] = Seq(Scope.Public)
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
    val body = Map(
      "client_id" -> appConfig.applicationAccessKey,
      "client_secret" -> appConfig.applicationSecret.getOrElse { throw new Exception("Missing secret.") },
      "redirect_uri" -> appConfig.oauthRedirectUri.getOrElse { throw new Exception("Missing redirect URI.") },
      "code" -> code,
      "grant_type" -> "authorization_code"
    ).asJson.toString
    httpClient.oauthPost[AccessToken]("/token", body)
  }
}
