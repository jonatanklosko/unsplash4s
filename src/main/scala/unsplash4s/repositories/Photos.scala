package unsplash4s.repositories

import io.circe.Json
import unsplash4s.HttpClient
import unsplash4s.entities.{Photo, SearchResult}
import unsplash4s.Decoders._

import scala.concurrent.Future

class Photos(
  httpClient: HttpClient
) {
  object PhotoOrderBy extends Enumeration {
    type PhotoOrderBy = Value
    val Latest = Value("latest")
    val Oldest = Value("oldest")
    val Popular = Value("popular")
  }
  import PhotoOrderBy.PhotoOrderBy

  object PhotoOrientation extends Enumeration {
    type PhotoOrientation = Value
    val Landscape = Value("landscape")
    val Portrait = Value("portrait")
    val Squarish = Value("squarish")
  }
  import PhotoOrientation.PhotoOrientation

  object ContentFilter extends Enumeration {
    type ContentFilter = Value
    val Low = Value("low")
    val High = Value("high")
  }
  import ContentFilter.ContentFilter

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
      "contentFilter" -> contentFilter
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
      "perPage" -> perPage,
      "collections" -> collectionIds.map(_.mkString(",")),
      "orientation" -> orientation,
      "contentFilter" -> contentFilter
    )
    httpClient.apiGet[SearchResult[Photo]](s"/search/photos", queryParams)
  }

  def likePhoto(id: String): Future[Json] = {
    /* TODO: return something else than JSON? */
    httpClient.apiPost[Json](s"/photos/$id/like", "")
  }

  def unlikePhoto(id: String): Future[Json] = {
    httpClient.apiDelete[Json](s"/photos/$id/like")
  }
}
