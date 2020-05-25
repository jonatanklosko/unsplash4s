package unsplash4s

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client._
import sttp.client.Response
import sttp.client.testing.SttpBackendStub
import sttp.model.StatusCode
import unsplash4s.entities.U4sError.NotFoundError

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.io.Source

class UnsplashSpec extends AnyFlatSpec with Matchers {
  "Unsplash photos.getPhotos" should "resolve to a list of photos" in {
    implicit val backend: SttpBackend[Future, Nothing, NothingT] = SttpBackendStub.asynchronousFuture.whenAnyRequest
      .thenRespondWrapped(Future {
        val photos = Source.fromResource("resources/photos.json")
        Response(photos.mkString, StatusCode.Ok)
      })

    val unsplashAppConfig: UnsplashAppConfig = UnsplashAppConfig(
      applicationAccessKey = "application access key",
      applicationSecret = Some("application secret"),
      oauthRedirectUri = Some("urn:ietf:wg:oauth:2.0:oob"),
      apiUrl = "https://api.example.com",
      oauthUrl = "https://oauth.example.com"
    );
    val unsplash = new Unsplash(unsplashAppConfig)
    val photosF = unsplash.photos.getPhotos()
    val photos = Await.result(photosF, 1.second)

    photos should have size 10
  }

  "Unsplash request" should "resolve to a Failure when rejected" in {
    implicit val backend: SttpBackend[Future, Nothing, NothingT] = SttpBackendStub.asynchronousFuture.whenAnyRequest
      .thenRespondWrapped(Future {
        Response("""{ "errors": ["Not found."] }""", StatusCode.NotFound)
      })

    val unsplashAppConfig: UnsplashAppConfig = UnsplashAppConfig(
      applicationAccessKey = "application access key",
      applicationSecret = Some("application secret"),
      oauthRedirectUri = Some("urn:ietf:wg:oauth:2.0:oob"),
      apiUrl = "https://api.example.com",
      oauthUrl = "https://oauth.example.com"
    );
    val unsplash = new Unsplash(unsplashAppConfig)
    val photosF = unsplash.photos.getPhotos()

    intercept[Exception] {
      Await.result(photosF, Duration.Inf)
    } shouldEqual NotFoundError("Not found.")
  }
}
