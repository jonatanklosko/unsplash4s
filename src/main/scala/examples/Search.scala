package examples

import unsplash4s.entities.SearchResult
import unsplash4s.{Unsplash, UnsplashAppConfig}

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object Search {
  def main(args: Array[String]): Unit = {
    val unsplashAppConfig = UnsplashAppConfig(
      applicationAccessKey = "q5d_MY49aqBFbJ5rY4KyyN_MHivhhwu4hbcJ3EMJUIk",
      applicationSecret = Some("OzReyy3mDJn2X2LOJpUdj9NksSNxZcWE0nrr2yovSYY"),
      oauthRedirectUri = Some("urn:ietf:wg:oauth:2.0:oob")
    );
    val unsplash = new Unsplash(unsplashAppConfig)

    unsplash.photos.searchPhotos("cats").onComplete {
      case Success(SearchResult(total, totalPages, results)) => {
        println(s"Photo results for search 'cats':\n- total: $total\n - total pages: $totalPages\n - this page: ${results.map(_.id).mkString(", ")}")
      }
      case Failure(error) => println((error))
    }

    unsplash.users.searchUsers("john").onComplete {
      case Success(SearchResult(total, totalPages, results)) => {
        println(s"User results for search 'john':\n- total: $total\n - total pages: $totalPages\n - this page: ${results.map(_.name).mkString(", ")}")
      }
      case Failure(error) => println((error))
    }

    unsplash.collections.searchCollections("football").onComplete {
      case Success(SearchResult(total, totalPages, results)) => {
        println(s"Collection results for search 'basketball':\n- total: $total\n - total pages: $totalPages\n - this page: ${results.map(_.title).mkString(", ")}")
      }
      case Failure(error) => println((error))
    }
  }
}
