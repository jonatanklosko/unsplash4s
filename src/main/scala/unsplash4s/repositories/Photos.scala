package unsplash4s.repositories

import unsplash4s.Client
import unsplash4s.entities.Photo

import scala.concurrent.Future

object Photos {
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

  def getPhoto(id: String)(implicit client: Client): Future[Photo] = {
    client.request[Photo](f"/photos/$id")
  }

  def getPhotos(
    page: Int = 1,
    perPage: Int = 10,
    orderBy: PhotoOrderBy = PhotoOrderBy.Latest
  )(implicit client: Client): Future[Seq[Photo]] = {
    val query = Map(
      "page" -> page,
      "per_page" -> perPage,
      "order_by" -> orderBy
    )
    client.request[Seq[Photo]]("/photos", query)
  }

  def getRandomPhotos(
    count: Int = 1,
    query: Option[String] = None,
    featured: Option[Boolean] = None,
    username: Option[String] = None,
    collectionIds: Option[Seq[Int]] = None,
    orientation: Option[PhotoOrientation] = None,
    contentFilter: Option[ContentFilter] = None
  )(implicit client: Client): Future[Seq[Photo]] = {
    val queryParams = Map(
      "count" -> count,
      "query" -> query,
      "featured" -> featured,
      "username" -> username,
      "collections" -> collectionIds.map(_.mkString(",")),
      "orientation" -> orientation,
      "contentFilter" -> contentFilter
    )
    client.request[Seq[Photo]]("/photos/random", queryParams)
  }

  def getUserPhotos(
    username: String,
    page: Int = 1,
    perPage: Int = 10,
    orderBy: PhotoOrderBy = PhotoOrderBy.Latest,
    orientation: Option[PhotoOrientation] = None
  )(implicit client: Client): Future[Seq[Photo]] = {
    val query = Map(
      "page" -> page,
      "per_page" -> perPage,
      "order_by" -> orderBy,
      "orientation" -> orientation
    )
    client.request[Seq[Photo]](f"/users/$username/photos", query)
  }
}
