package unsplash4s.integration

import sttp.client.Response
import sttp.model.StatusCode
import unsplash4s.utils.BaseIntegrationSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.io.Source

class PhotosSpec extends BaseIntegrationSpec {
  "Photos > getPhotos" should "return a future resolving to a list of photos" in {
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(_.uri.path == List("photos"))
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/photos.json").mkString
          Response(json, StatusCode.Ok)
        })
    })

    val photosF = unsplash.photos.getPhotos()
    val photos = Await.result(photosF, 1.second)

    photos should have size 10
    photos.head.id shouldEqual "rPuFG8POZ5M"
  }

  "Photos > getPhoto" should "return a future resolving to the specified photo" in {
    val photoId = "9f3ugLFUpOw"
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(_.uri.path == List("photos", photoId))
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/photo.json").mkString
          Response(json, StatusCode.Ok)
        })
    })

    val photoF = unsplash.photos.getPhoto(photoId)
    val photo = Await.result(photoF, 1.second)

    photo.id shouldEqual photoId
  }

  "Photos > getRandomPhotos" should "return a future resolving to a list of random photos" in {
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(_.uri.path == List("photos", "random"))
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/photos-random.json").mkString
          Response(json, StatusCode.Ok)
        })
    })

    val photosF = unsplash.photos.getRandomPhotos()
    val photos = Await.result(photosF, 1.second)

    photos should have size 1
    photos.head.id shouldEqual "jdRxqFpPWkU"
  }

  "Photos > getUserPhotos" should "return a future resolving to a list of user photos" in {
    val username = "thedakotacorbin"
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(_.uri.path == List("users", username, "photos"))
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/user-photos.json").mkString
          Response(json, StatusCode.Ok)
        })
    })

    val photosF = unsplash.photos.getUserPhotos(username)
    val photos = Await.result(photosF, 1.second)

    photos should have size 10
    photos.head.user.username shouldEqual username
  }

  "Photos > getCollectionPhotos" should "return a future resolving to a list of collection photos" in {
    val collectionId = 9454911
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(_.uri.path == List("collections", collectionId.toString, "photos"))
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/collection-photos.json").mkString
          Response(json, StatusCode.Ok)
        })
    })

    val photosF = unsplash.photos.getCollectionPhotos(9454911)
    val photos = Await.result(photosF, 1.second)

    photos should have size 10
    photos.head.id shouldEqual "8hW2ZB4OHZ0"
  }

  "Photos > searchPhotos" should "return a future resolving to a list of matching photos" in {
    val query = "cat"
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(req => {
          req.uri.path == List("search", "photos") &&
            req.uri.params.get("query").getOrElse(throw new Exception("Query param not found.")) == query
        })
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/search-photos.json").mkString
          Response(json, StatusCode.Ok)
        })
    })

    val searchResultF = unsplash.photos.searchPhotos(query)
    val searchResult = Await.result(searchResultF, 1.second)

    searchResult.total shouldEqual 10376
    searchResult.totalPages shouldEqual 1038
    searchResult.results should have size 10
  }

  "Photo > getPhotoStatistics" should "return a future resolving to a list of statistic data" in {
    val id = "9SWHIgu8A8k""
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(_.uri.path == List("photos", id, "statistics"))
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/photo-statistics.json").mkString
          Response(json, StatusCode.Ok)
        })
    })
    val statisticsResultF = unsplash.photos.getPhotoStatistics(id)
    val statisticsResult = Await.result(statisticsResultF, 1.second)

    statisticsResult.downloads.total shouldEqual 50489
    statisticsResult.downloads.historical.change shouldEqual 6740
    statisticsResult.downloads.historical.values(0).value shouldEqual 239
    statisticsResult.views.total shouldEqual 13570670
    statisticsResult.views.historical.change shouldEqual 1903051
    statisticsResult.views.historical.values(0).value shouldEqual 50834
    statisticsResult.likes.total shouldEqual 712
    statisticsResult.likes.historical.change shouldEqual  56
    statisticsResult.likes.historical.values(0).value shouldEqual 0
  }
}
