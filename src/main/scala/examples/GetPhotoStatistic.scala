package examples

import unsplash4s.Unsplash

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object GetPhotoStatistic {
  def main(args: Array[String]): Unit = {
    val unsplash = new Unsplash(Examples.unsplashAppConfig)

    unsplash.photos.getPhotoStatistics("xulIYVIbYIc").onComplete {
      case Success(collections) => println(
        "Statictics: \n Downloads: " + collections.downloads +
        "\n Likes: " + collections.likes +
        "\n Views: " + collections.views)
      case Failure(error) => println(error)
    }
  }
}
