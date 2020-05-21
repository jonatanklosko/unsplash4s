package unsplash4s.repositories

import unsplash4s.HttpClient
import unsplash4s.entities.{Photo, SearchResult, User}
import unsplash4s.Decoders._

import scala.concurrent.Future

class Users(
  httpClient: HttpClient
) {
  def getUser(username: String): Future[User] = {
    httpClient.apiGet[User](s"/users/$username")
  }

  def searchUsers(
    query: String,
    page: Int = 1,
    perPage: Int = 10
  ): Future[SearchResult[User]] = {
    val queryParams = Map(
      "query" -> query,
      "page" -> page,
      "perPage" -> perPage
    )
    httpClient.apiGet[SearchResult[User]](s"/search/users", queryParams)
  }
}
