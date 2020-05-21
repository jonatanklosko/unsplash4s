package unsplash4s.repositories

import unsplash4s.HttpClient
import unsplash4s.entities.{Collection, SearchResult}
import unsplash4s.Decoders._

import scala.concurrent.Future

class Collections(
  httpClient: HttpClient
) {
  def getCollection(id: Int): Future[Collection] = {
    httpClient.apiGet[Collection](s"/collections/$id")
  }

  def getCollections(
    page: Int = 1,
    perPage: Int = 10
  ): Future[Seq[Collection]] = {
    val query = Map(
      "page" -> page,
      "per_page" -> perPage
    )
    httpClient.apiGet[Seq[Collection]]("/collections", query)
  }

  def getFeaturedCollections(
    page: Int = 1,
    perPage: Int = 10
  ): Future[Seq[Collection]] = {
    val query = Map(
      "page" -> page,
      "per_page" -> perPage
    )
    httpClient.apiGet[Seq[Collection]]("/collections/featured", query)
  }

  def searchCollections(
    query: String,
    page: Int = 1,
    perPage: Int = 10
  ): Future[SearchResult[Collection]] = {
    val queryParams = Map(
      "query" -> query,
      "page" -> page,
      "perPage" -> perPage
    )
    httpClient.apiGet[SearchResult[Collection]](s"/search/collections", queryParams)
  }

  def getRelatedCollections(
    collectionId: Int
  ): Future[Seq[Collection]] = {
    httpClient.apiGet[Seq[Collection]](s"/collections/$collectionId/related")
  }
}
