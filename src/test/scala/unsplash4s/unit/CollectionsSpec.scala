package unsplash4s.unit

import unsplash4s.entities.Collection
import unsplash4s.repositories.Collections
import unsplash4s.utils.BaseUnitSpec

class CollectionsSpec extends BaseUnitSpec {
  "Collections > getCollection" should "call apiGet on http client" in {
    val httpClient = httpClientExpectingApiGet[Collection]("/collections/10")
    val collections = new Collections(httpClient)

    collections.getCollection(10)
  }

  "Collections > getCollections" should "call apiGet on http client with default params" in {
    val httpClient = httpClientExpectingApiGet[Collection](
      "/collections",
      Map(
        "page" -> "1",
        "per_page" -> "10"
      )
    )
    val collections = new Collections(httpClient)

    collections.getCollections()
  }

  it should "allow for specifying page and per page" in {
    val httpClient = httpClientExpectingApiGet[Seq[Collection]](
      "/collections",
      Map(
        "page" -> "5",
        "per_page" -> "20"
      )
    )
    val collections = new Collections(httpClient)

    collections.getCollections(page = 5, perPage = 20)
  }

  "Collections > getFeaturedCollections" should "call apiGet on http client with default params" in {
    val httpClient = httpClientExpectingApiGet[Collection](
      "/collections/featured",
      Map(
        "page" -> "1",
        "per_page" -> "10"
      )
    )
    val collections = new Collections(httpClient)

    collections.getFeaturedCollections()
  }

  it should "allow for specifying page and per page" in {
    val httpClient = httpClientExpectingApiGet[Seq[Collection]](
      "/collections/featured",
      Map(
        "page" -> "5",
        "per_page" -> "20"
      )
    )
    val collections = new Collections(httpClient)

    collections.getFeaturedCollections(page = 5, perPage = 20)
  }

  "Collections > searchCollections" should "call apiGet on http client with query and default params" in {
    val httpClient = httpClientExpectingApiGet[Seq[Collection]](
      "/search/collections",
      Map(
        "query" -> "cats",
        "page" -> "1",
        "per_page" -> "10"
      )
    )
    val collections = new Collections(httpClient)

    collections.searchCollections("cats")
  }

  it should "allow for specifying page and per page" in {
    val httpClient = httpClientExpectingApiGet[Seq[Collection]](
      "/search/collections",
      Map(
        "query" -> "cats",
        "page" -> "2",
        "per_page" -> "20"
      )
    )
    val collections = new Collections(httpClient)

    collections.searchCollections("cats", page = 2, perPage = 20)
  }

  "Collections > getRelatedCollections" should "call apiGet on http client with the given collection" in {
    val httpClient = httpClientExpectingApiGet[Seq[Collection]](
      "/collections/100/related"
    )
    val collections = new Collections(httpClient)

    collections.getRelatedCollections(100)
  }
}
