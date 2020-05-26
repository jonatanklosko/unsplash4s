package unsplash4s.integration
import sttp.client.Response
import sttp.model.StatusCode
import unsplash4s.entities.U4sError.{ForbiddenError, JsonParsingError, NotFoundError, UnauthorizedError, UnhandledResponseError}
import unsplash4s.utils.BaseIntegrationSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class ErrorsSpec extends BaseIntegrationSpec {
  "Any unauthorized request" should "return a future rejected with UnauthorizedError" in {
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenAnyRequest
        .thenRespondWrapped(Future {
          val json = """{ "errors": ["Not authorized."] }"""
          Response(json, StatusCode.Unauthorized)
        })
    })

    intercept[Exception] {
      Await.result(unsplash.photos.getPhotos(), 1.second)
    } shouldEqual UnauthorizedError("Not authorized.")
  }

  "Any forbidden request" should "return a future rejected with ForbiddenError" in {
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenAnyRequest
        .thenRespondWrapped(Future {
          val json = """{ "errors": ["Forbidden."] }"""
          Response(json, StatusCode.Forbidden)
        })
    })

    intercept[Exception] {
      Await.result(unsplash.photos.getPhotos(), 1.second)
    } shouldEqual ForbiddenError("Forbidden.")
  }

  "Any not found request" should "return a future rejected with NotFoundError" in {
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenAnyRequest
        .thenRespondWrapped(Future {
          val json = """{ "errors": ["Not found."] }"""
          Response(json, StatusCode.NotFound)
        })
    })

    intercept[Exception] {
      Await.result(unsplash.photos.getPhotos(), 1.second)
    } shouldEqual NotFoundError("Not found.")
  }

  "Any unhandled error request" should "return a future rejected with UnhandledResponseError" in {
    val json = """{ "errors": ["Something went wrong."] }"""
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenAnyRequest
        .thenRespondWrapped(Future {
          Response(json, StatusCode.InternalServerError)
        })
    })

    intercept[Exception] {
      Await.result(unsplash.photos.getPhotos(), 1.second)
    } shouldEqual UnhandledResponseError("Something went wrong.", json)
  }


  "Any request with parsing failure" should "return a future rejected with JsonParsingError" in {
    val json = """{ "id": 1"""
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenAnyRequest
        .thenRespondWrapped(Future {
          Response(json, StatusCode.Ok)
        })
    })

    intercept[Exception] {
      Await.result(unsplash.photos.getPhotos(), 1.second)
    } shouldBe a[JsonParsingError]
  }
}
