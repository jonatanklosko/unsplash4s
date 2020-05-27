package unsplash4s.unit

import unsplash4s.entities.User
import unsplash4s.repositories.Users
import unsplash4s.utils.BaseUnitSpec

class UsersSpec extends BaseUnitSpec {
  "Users > getUser" should "call apiGet on http client" in {
    val httpClient = httpClientExpectingApiGet[User]("/users/sherlock")
    val users = new Users(httpClient)

    users.getUser("sherlock")
  }

  "Users > searchUsers" should "call apiGet on http client with query and default params" in {
    val httpClient = httpClientExpectingApiGet[Seq[User]](
      "/search/users",
      Map(
        "query" -> "john",
        "page" -> "1",
        "per_page" -> "10"
      )
    )
    val users = new Users(httpClient)

    users.searchUsers("john")
  }

  it should "allow for specifying page and per page" in {
    val httpClient = httpClientExpectingApiGet[Seq[User]](
      "/search/users",
      Map(
        "query" -> "john",
        "page" -> "5",
        "per_page" -> "20"
      )
    )
    val users = new Users(httpClient)

    users.searchUsers("john", page = 5, perPage = 20)
  }
}
