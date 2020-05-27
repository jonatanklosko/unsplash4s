package unsplash4s.repositories

import io.circe.Json
import unsplash4s.Decoders._
import unsplash4s.HttpClient
import unsplash4s.entities.{Photo, SearchResult, Statistic}
import unsplash4s.repositories.Photos.ContentFilter.ContentFilter
import unsplash4s.repositories.Photos.PhotoOrderBy
import unsplash4s.repositories.Photos.PhotoOrderBy.PhotoOrderBy
import unsplash4s.repositories.Photos.PhotoOrientation.PhotoOrientation

import scala.concurrent.{ExecutionContext, Future}

class Photos(
  httpClient: HttpClient
)(
  implicit ec: ExecutionContext = ExecutionContext.global
) {
  def getPhoto(id: String): Future[Photo] = {
    httpClient.apiGet[Photo](s"/photos/$id")
  }

  def getPhotos(
    page: Int = 1,
    perPage: Int = 10,
    orderBy: PhotoOrderBy = PhotoOrderBy.Latest
  ): Future[Seq[Photo]] = {
    val query = Map(
      "page" -> page,
      "per_page" -> perPage,
      "order_by" -> orderBy
    )
    httpClient.apiGet[Seq[Photo]]("/photos", query)
  }

  def getRandomPhotos(
    count: Int = 1,
    query: Option[String] = None,
    featured: Option[Boolean] = None,
    username: Option[String] = None,
    collectionIds: Option[Seq[Int]] = None,
    orientation: Option[PhotoOrientation] = None,
    contentFilter: Option[ContentFilter] = None
  ): Future[Seq[Photo]] = {
    val queryParams = Map(
      "count" -> count,
      "query" -> query,
      "featured" -> featured,
      "username" -> username,
      "collections" -> collectionIds.map(_.mkString(",")),
      "orientation" -> orientation,
      "content_filter" -> contentFilter
    )
    httpClient.apiGet[Seq[Photo]]("/photos/random", queryParams)
  }

  def getUserPhotos(
    username: String,
    page: Int = 1,
    perPage: Int = 10,
    orderBy: PhotoOrderBy = PhotoOrderBy.Latest,
    orientation: Option[PhotoOrientation] = None
  ): Future[Seq[Photo]] = {
    val query = Map(
      "page" -> page,
      "per_page" -> perPage,
      "order_by" -> orderBy,
      "orientation" -> orientation
    )
    httpClient.apiGet[Seq[Photo]](s"/users/$username/photos", query)
  }

  def getCollectionPhotos(
    collectionId: Int,
    page: Int = 1,
    perPage: Int = 10,
    orientation: Option[PhotoOrientation] = None
  ): Future[Seq[Photo]] = {
    val query = Map(
      "page" -> page,
      "per_page" -> perPage,
      "orientation" -> orientation
    )
    httpClient.apiGet[Seq[Photo]](s"/collections/$collectionId/photos", query)
  }

  def searchPhotos(
    query: String,
    page: Int = 1,
    perPage: Int = 10,
    collectionIds: Option[Seq[Int]] = None,
    orientation: Option[PhotoOrientation] = None,
    contentFilter: Option[ContentFilter] = None
  ): Future[SearchResult[Photo]] = {
    val queryParams = Map(
      "query" -> query,
      "page" -> page,
      "per_page" -> perPage,
      "collections" -> collectionIds.map(_.mkString(",")),
      "orientation" -> orientation,
      "content_filter" -> contentFilter
    )
    httpClient.apiGet[SearchResult[Photo]](s"/search/photos", queryParams)
  }

  def likePhoto(id: String): Future[Unit] = {
    httpClient.apiPost[Json](s"/photos/$id/like", None)
      .map(_ => ())
  }

  def unlikePhoto(id: String): Future[Unit] = {
    httpClient.apiDelete[Json](s"/photos/$id/like")
      .map(_ => ())
  }

  def triggerPhotoDownload(id: String): Future[Unit] = {
    httpClient.apiGet[Json](s"/photos/$id/download")
      .map(_ => ())
  }

  def getPhotoStatistics(id: String): Future[Statistic] = {
    httpClient.apiGet[Statistic](s"/photos/$id/statistics")
  }
}

object Photos {
  object PhotoOrderBy extends Enumeration {
    type PhotoOrderBy = Value
    val Latest = Value("latest")
    val Oldest = Value("oldest")
    val Popular = Value("popular")
  }

  object PhotoOrientation extends Enumeration {
    type PhotoOrientation = Value
    val Landscape = Value("landscape")
    val Portrait = Value("portrait")
    val Squarish = Value("squarish")
  }

  object ContentFilter extends Enumeration {
    type ContentFilter = Value
    val Low = Value("low")
    val High = Value("high")
  }
}
