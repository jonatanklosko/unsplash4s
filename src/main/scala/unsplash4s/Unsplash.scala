package unsplash4s

import sttp.client.{NothingT, SttpBackend}
import sttp.client.asynchttpclient.future.AsyncHttpClientFutureBackend
import unsplash4s.repositories.{Collections, OAuth, Photos, Users}
import scala.concurrent.Future

class Unsplash(
  appConfig: UnsplashAppConfig,
  accessToken: Option[String] = None
)(
  implicit sttpBackend: SttpBackend[Future, Nothing, NothingT] = AsyncHttpClientFutureBackend()
) {
  val httpClient = new HttpClient(appConfig, accessToken)

  lazy val oauth = new OAuth(httpClient, appConfig)
  lazy val photos = new Photos(httpClient)
  lazy val users = new Users(httpClient)
  lazy val collections = new Collections(httpClient)
}
