package examples

import unsplash4s.Unsplash

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object GetCollectionPhotos {
  def main(args: Array[String]): Unit = {
    val unsplash = new Unsplash(Examples.unsplashAppConfig)

    unsplash.photos.getCollectionPhotos(9454911).onComplete {
      case Success(photos) => println(photos.map(_.id).mkString(", "))
      case Failure(error) => println(error)
    }
  }
}
