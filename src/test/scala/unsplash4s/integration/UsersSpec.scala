package unsplash4s.integration

import sttp.client.Response
import sttp.model.StatusCode
import unsplash4s.utils.BaseIntegrationSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.io.Source

class UsersSpec extends BaseIntegrationSpec {
  "Users > getUser" should "return a future resolving to the specified user" in {
    val username = "thedakotacorbin"
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(_.uri.path == List("users", username))
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/user.json").mkString
          Response(json, StatusCode.Ok)
        })
    })

    val userF = unsplash.users.getUser(username)
    val user = Await.result(userF, 1.second)

    user.username shouldEqual username
  }

  "Users > searchUsers" should "return a future resolving to a list of matching users" in {
    val query = "john"
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(req => {
          req.uri.path == List("search", "users") &&
            req.uri.params.get("query").getOrElse(throw new Exception("Query param not found.")) == query
        })
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/search-users.json").mkString
          Response(json, StatusCode.Ok)
        })
    })

    val searchResultF = unsplash.users.searchUsers(query)
    val searchResult = Await.result(searchResultF, 1.second)

    searchResult.total shouldEqual 18190
    searchResult.totalPages shouldEqual 1819
    searchResult.results should have size 10
  }

  "Users > getUserStatistics" should "return a future resolving to a list of statistic data" in {
    val username = "ramche"
    val unsplash = getUnsplash(stubBackend => {
      stubBackend
        .whenRequestMatches(_.uri.path == List("users", username, "statistics"))
        .thenRespondWrapped(Future {
          val json = Source.fromResource("responses/user-statistics.json").mkString
          Response(json, StatusCode.Ok)
        })
    })
    val statisticsResultF = unsplash.users.getUserStatistics(username)
    val statisticsResult = Await.result(statisticsResultF, 1.second)

    statisticsResult.downloads.total shouldEqual 241570
    statisticsResult.downloads.historical.change shouldEqual 21319
    statisticsResult.downloads.historical.values(0).value shouldEqual 923
    statisticsResult.views.total shouldEqual 58837477
    statisticsResult.views.historical.change shouldEqual 4845035
    statisticsResult.views.historical.values(0).value shouldEqual 195672
    statisticsResult.likes.total shouldEqual 5340
    statisticsResult.likes.historical.change shouldEqual 296
    statisticsResult.likes.historical.values(0).value shouldEqual 9
  }
}
