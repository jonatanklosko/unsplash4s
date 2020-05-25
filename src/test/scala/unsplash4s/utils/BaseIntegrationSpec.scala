package unsplash4s.utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client.testing.SttpBackendStub
import sttp.client.{NothingT, SttpBackend}
import unsplash4s.{Unsplash, UnsplashAppConfig}

import scala.concurrent.Future

class BaseIntegrationSpec extends AnyFlatSpec with Matchers {
  def getUnsplash(configureBackend: SttpBackendStub[Future, Nothing] => SttpBackendStub[Future, Nothing]): Unsplash = {
    implicit val backend: SttpBackend[Future, Nothing, NothingT] = configureBackend(SttpBackendStub.asynchronousFuture)

    val unsplashAppConfig: UnsplashAppConfig = UnsplashAppConfig(
      applicationAccessKey = "application access key",
      applicationSecret = Some("application secret"),
      oauthRedirectUri = Some("urn:ietf:wg:oauth:2.0:oob"),
      apiUrl = "https://api.example.com",
      oauthUrl = "https://oauth.example.com"
    );
    new Unsplash(unsplashAppConfig)
  }
}
