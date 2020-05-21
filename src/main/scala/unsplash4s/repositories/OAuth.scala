package unsplash4s.repositories

import io.circe.syntax._
import sttp.client._
import unsplash4s.Client
import unsplash4s.entities.AccessToken
import unsplash4s.Decoders._
import unsplash4s.entities.AccessToken.Scope
import unsplash4s.entities.AccessToken.Scope.Scope

import scala.concurrent.Future

object OAuth {
  def authorizationUrl(
    scope: Seq[Scope] = Seq(Scope.Public)
  )(implicit client: Client): String = {
    val query = Map(
      "client_id" -> client.applicationAccessKey,
      "redirect_uri" -> client.oauthRedirectUri,
      "response_type" -> "code",
      "scope" -> scope.mkString(" ")
    )
    val url = uri"${client.oauthUrl}/authorize?$query"
    url.toString
  }

  def getAccessToken(code: String)(implicit client: Client): Future[AccessToken] = {
    val body = Map(
      "client_id" -> client.applicationAccessKey,
      "client_secret" -> client.applicationSecret.getOrElse { throw new Exception("Missing secret.") },
      "redirect_uri" -> client.oauthRedirectUri.getOrElse { throw new Exception("Missing redirect URI.") },
      "code" -> code,
      "grant_type" -> "authorization_code"
    ).asJson.toString
    val url = uri"${client.oauthUrl}/token"
    client.postRequest[AccessToken](url, body)
  }
}
