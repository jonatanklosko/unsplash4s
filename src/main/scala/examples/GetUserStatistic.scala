package examples

import unsplash4s.Unsplash
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object GetUserStatistic {
  def main(args: Array[String]): Unit = {
    val unsplash = new Unsplash(Examples.unsplashAppConfig)

    unsplash.users.getUserStatistics("am_renae").onComplete {
      case Success(collections) => println(
        "Statictics: \n Downloads: " + collections.downloads +
          "\n Likes: " + collections.likes +
          "\n Views: " + collections.views)
      case Failure(error) => println(error)
    }
  }
}
