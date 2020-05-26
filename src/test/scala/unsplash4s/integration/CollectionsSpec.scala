package unsplash4s.integration

import sttp.client.Response
import sttp.model.StatusCode
import unsplash4s.utils.BaseIntegrationSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.io.Source

class CollectionsSpec extends BaseIntegrationSpec {
  "Collections > getCollection" should "return a future resolving to the specified collection" in {
    val collectionId = 9454911
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(_.uri.path == List("collections", collectionId.toString))
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/collection.json").mkString
          Response(json, StatusCode.Ok)
        })
    })

    val collectionF = unsplash.collections.getCollection(collectionId)
    val collection = Await.result(collectionF, 1.second)

    collection.id shouldEqual collectionId
    collection.title shouldEqual "\u2013\u2013BASKETBALL"
  }

  "Collections > getCollections" should "return a future resolving to a list of collections" in {
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(_.uri.path == List("collections"))
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/collections.json").mkString
          Response(json, StatusCode.Ok)
        })
    })

    val collectionsF = unsplash.collections.getCollections()
    val collections = Await.result(collectionsF, 1.second)

    collections should have size 10
    collections.head.id shouldEqual 9454911
  }

  "Collections > getFeaturedCollections" should "return a future resolving to a list of featured collections" in {
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(_.uri.path == List("collections", "featured"))
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/collections-featured.json").mkString
          Response(json, StatusCode.Ok)
        })
    })

    val collectionsF = unsplash.collections.getFeaturedCollections()
    val collections = Await.result(collectionsF, 1.second)

    collections should have size 10
    collections.head.id shouldEqual 9454911
  }

  "Collections > searchCollections" should "return a future resolving to a list of matching collections" in {
    val query = "cat"
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(req => {
          req.uri.path == List("search", "collections") &&
            req.uri.params.get("query").getOrElse(throw new Exception("Query param not found.")) == query
        })
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/search-collections.json").mkString
          Response(json, StatusCode.Ok)
        })
    })

    val searchResultF = unsplash.collections.searchCollections(query)
    val searchResult = Await.result(searchResultF, 1.second)

    searchResult.total shouldEqual 41769
    searchResult.totalPages shouldEqual 4177
    searchResult.results should have size 10
  }

  "Collections > getRelatedCollections" should "return a future resolving to a list of collections related to the given one" in {
    val collectionId = 9454911
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(_.uri.path == List("collections", collectionId.toString, "related"))
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/collections-related.json").mkString
          Response(json, StatusCode.Ok)
        })
    })

    val collectionsF = unsplash.collections.getRelatedCollections(collectionId)
    val collections = Await.result(collectionsF, 1.second)

    collections should have size 3
    collections.head.id shouldEqual 241823
  }
}
