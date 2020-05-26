package unsplash4s.repositories

import unsplash4s.Decoders._
import unsplash4s.HttpClient
import unsplash4s.entities.{SearchResult, Statistic, User}

import scala.concurrent.Future

class Users(
  httpClient: HttpClient
) extends BaseRepository {
  def getUser(username: String): Future[User] = {
    httpClient.apiGet[User](s"/users/$username")
  }

  def searchUsers(
    query: String,
    page: Int = 1,
    perPage: Int = 10
  ): Future[SearchResult[User]] = {
    val queryParams = queryParamsMap(
      "query" -> query,
      "page" -> page,
      "per_page" -> perPage
    )
    httpClient.apiGet[SearchResult[User]](s"/search/users", queryParams)
  }

  def getUserStatistics(username: String): Future[Statistic] = {
    httpClient.apiGet[Statistic](s"/users/$username/statistics")
  }
}
