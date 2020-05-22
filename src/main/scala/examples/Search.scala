package examples

import unsplash4s.Unsplash
import unsplash4s.entities.SearchResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Search {
  def main(args: Array[String]): Unit = {
    val unsplash = new Unsplash(Examples.unsplashAppConfig)

    unsplash.photos.searchPhotos("cats").onComplete {
      case Success(SearchResult(total, totalPages, results)) => {
        println(s"Photo results for search 'cats':\n- total: $total\n - total pages: $totalPages\n - this page: ${results.map(_.id).mkString(", ")}")
      }
      case Failure(error) => println(error)
    }

    unsplash.users.searchUsers("john").onComplete {
      case Success(SearchResult(total, totalPages, results)) => {
        println(s"User results for search 'john':\n- total: $total\n - total pages: $totalPages\n - this page: ${results.map(_.name).mkString(", ")}")
      }
      case Failure(error) => println(error)
    }

    unsplash.collections.searchCollections("football").onComplete {
      case Success(SearchResult(total, totalPages, results)) => {
        println(s"Collection results for search 'basketball':\n- total: $total\n - total pages: $totalPages\n - this page: ${results.map(_.title).mkString(", ")}")
      }
      case Failure(error) => println(error)
    }
  }
}
