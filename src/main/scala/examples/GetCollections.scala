package examples

import unsplash4s.Unsplash

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object GetCollections {
  def main(args: Array[String]): Unit = {
    val unsplash = new Unsplash(Examples.unsplashAppConfig)

    unsplash.collections.getCollections().onComplete {
      case Success(collections) => println("Collections: " +collections.map(_.title).mkString(", "))
      case Failure(error) => println(error)
    }

    unsplash.collections.getFeaturedCollections().onComplete {
      case Success(collections) => println("Featured: " + collections.map(_.title).mkString(", "))
      case Failure(error) => println(error)
    }

    unsplash.collections.getCollection(9454911).onComplete {
      case Success(collection) => println("Collection photos: " + collection.title)
      case Failure(error) => println(error)
    }

    unsplash.collections.getRelatedCollections(9454911).onComplete {
      case Success(collections) => println("Related: " + collections.map(_.title).mkString(", "))
      case Failure(error) => println(error)
    }
  }
}
