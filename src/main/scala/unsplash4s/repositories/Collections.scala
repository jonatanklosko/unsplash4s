package unsplash4s.repositories

import io.circe.Json
import io.circe.syntax._
import unsplash4s.Decoders._
import unsplash4s.HttpClient
import unsplash4s.entities.{Collection, SearchResult}

import scala.concurrent.{ExecutionContext, Future}

class Collections(
  httpClient: HttpClient
)(
  implicit ec: ExecutionContext = ExecutionContext.global
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
      "per_page" -> perPage
    )
    httpClient.apiGet[SearchResult[Collection]](s"/search/collections", queryParams)
  }

  def getRelatedCollections(
    id: Int
  ): Future[Seq[Collection]] = {
    httpClient.apiGet[Seq[Collection]](s"/collections/$id/related")
  }

  def createCollection(
    title: String,
    description: Option[String] = None,
    `private`: Boolean = false
  ): Future[Collection] = {
    val body = Json.obj(
      "title" -> title.asJson,
      "description" -> description.asJson,
      "private" -> `private`.asJson
    ).toString
    httpClient.apiPost[Collection](s"/collections", Some(body))
  }

  def deleteCollection(id: Int): Future[Unit] = {
    httpClient.apiDelete[Json](s"/collections/$id")
      .map(_ => ())
  }

  def addPhotoToCollection(
    id: Int,
    photoId: String
  ): Future[Unit] = {
    val body = Json.obj(
      "photo_id" -> photoId.asJson
    ).toString
    httpClient.apiPost[Json](s"/collections/$id/add", Some(body))
      .map(_ => ())
  }

  def removePhotoFromCollection(
    id: Int,
    photoId: String
  ): Future[Unit] = {
    val query = Map(
      "photo_id" -> photoId,
    )
    httpClient.apiDelete[Json](s"/collections/$id/remove", query)
      .map(_ => ())
  }
}
