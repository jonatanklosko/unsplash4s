package unsplash4s.unit

import unsplash4s.entities.Photo
import unsplash4s.repositories.Photos
import unsplash4s.repositories.Photos.ContentFilter.ContentFilter
import unsplash4s.repositories.Photos.PhotoOrientation.PhotoOrientation
import unsplash4s.repositories.Photos.{ContentFilter, PhotoOrderBy, PhotoOrientation}
import unsplash4s.utils.BaseUnitSpec

class PhotosSpec extends BaseUnitSpec {
  "Photo > getPhoto" should "call apiGet on http client" in {
    val httpClient = httpClientExpectingApiGet[Photo]("/photos/11")
    val photos = new Photos(httpClient)

    photos.getPhoto("11")
  }

  "Photo > getPhotos" should "call apiGet with default params" in {
    val httpClient = httpClientExpectingApiGet[Photo](
      "/photos",
      Map(
        "page" -> "1",
        "per_page" -> "10",
        "order_by" -> "latest"
      )
    )
    val photos = new Photos(httpClient)

    photos.getPhotos()
  }

  it should "allow for specifying page, per page and order" in {
    val httpClient = httpClientExpectingApiGet[Photo](
      "/photos",
      Map(
        "page" -> "5",
        "per_page" -> "20",
        "order_by" -> "popular"
      )
    )
    val photos = new Photos(httpClient)

    photos.getPhotos(page = 5, perPage = 20, orderBy = PhotoOrderBy.Popular)
  }

  "Photo > getRandomPhotos" should "call apiGet with default params" in {
    val httpClient = httpClientExpectingApiGet[Photo](
      "/photos/random",
      Map("count" -> "1")
    )
    val photos = new Photos(httpClient)

    photos.getRandomPhotos()
  }

  it should "allow for specifying query params" in {
    val httpClient = httpClientExpectingApiGet[Photo](
      "/photos/random",
      Map(
        "count" -> "2",
        "query" -> "cat",
        "featured" -> "true",
        "username" -> "sherlock",
        "collections" -> "10,200",
        "orientation" -> "landscape",
        "content_filter" -> "low"
      )
    )
    val photos = new Photos(httpClient)

    photos.getRandomPhotos(
      count = 2,
      query = Some("cat"),
      featured = Some(true),
      username = Some("sherlock"),
      collectionIds = Some(Seq(10, 200)),
      orientation = Some(PhotoOrientation.Landscape),
      contentFilter = Some(ContentFilter.Low)
    )
  }

  "Photo > getUserPhotos" should "call apiGet with default params" in {
    val httpClient = httpClientExpectingApiGet[Photo](
      "/users/sherlock/photos",
      Map(
        "page" -> "1",
        "per_page" -> "10",
        "order_by" -> "latest"
      )
    )
    val photos = new Photos(httpClient)

    photos.getUserPhotos("sherlock")
  }

  it should "allow for specifying query params" in {
    val httpClient = httpClientExpectingApiGet[Photo](
      "/users/sherlock/photos",
      Map(
        "page" -> "2",
        "per_page" -> "20",
        "order_by" -> "popular",
        "orientation" -> "landscape"
      )
    )
    val photos = new Photos(httpClient)

    photos.getUserPhotos(
      "sherlock",
      page = 2,
      perPage = 20,
      orderBy = PhotoOrderBy.Popular,
      orientation = Some(PhotoOrientation.Landscape)
    )
  }

  "Photo > getCollectionPhotos" should "call apiGet with default params" in {
    val httpClient = httpClientExpectingApiGet[Photo](
      "/collections/111/photos",
      Map(
        "page" -> "1",
        "per_page" -> "10"
      )
    )
    val photos = new Photos(httpClient)

    photos.getCollectionPhotos(111)
  }

  it should "allow for specifying query params" in {
    val httpClient = httpClientExpectingApiGet[Photo](
      "/collections/111/photos",
      Map(
        "page" -> "5",
        "per_page" -> "20",
        "orientation" -> "portrait"
      )
    )
    val photos = new Photos(httpClient)

    photos.getCollectionPhotos(
      111,
      page = 5,
      perPage = 20,
      orientation = Some(PhotoOrientation.Portrait)
    )
  }

  "Photo > searchPhotos" should "call apiGet with search and default params" in {
    val httpClient = httpClientExpectingApiGet[Photo](
      "/search/photos",
      Map(
        "query" -> "cats",
        "page" -> "1",
        "per_page" -> "10"
      )
    )
    val photos = new Photos(httpClient)

    photos.searchPhotos("cats")
  }

  it should "allow for specifying query params" in {
    val httpClient = httpClientExpectingApiGet[Photo](
      "/search/photos",
      Map(
        "query" -> "cats",
        "page" -> "2",
        "per_page" -> "20",
        "collections" -> "4,10",
        "orientation" -> "squarish",
        "content_filter" -> "high"
      )
    )
    val photos = new Photos(httpClient)

    photos.searchPhotos(
      "cats",
      page = 2,
      perPage = 20,
      collectionIds = Some(Seq(4, 10)),
      orientation = Some(PhotoOrientation.Squarish),
      contentFilter = Some(ContentFilter.High)
    )
  }
}
