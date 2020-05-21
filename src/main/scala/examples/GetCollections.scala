package examples

import unsplash4s.{Unsplash, UnsplashAppConfig}

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object GetCollections {
  def main(args: Array[String]): Unit = {
    val unsplashAppConfig = UnsplashAppConfig(
      applicationAccessKey = "q5d_MY49aqBFbJ5rY4KyyN_MHivhhwu4hbcJ3EMJUIk",
      applicationSecret = Some("OzReyy3mDJn2X2LOJpUdj9NksSNxZcWE0nrr2yovSYY"),
      oauthRedirectUri = Some("urn:ietf:wg:oauth:2.0:oob")
    );
    val unsplash = new Unsplash(unsplashAppConfig)

    unsplash.collections.getCollections().onComplete {
      case Success(collections) => println("Collections: " +collections.map(_.title).mkString(", "))
      case Failure(error) => println((error))
    }

    unsplash.collections.getFeaturedCollections().onComplete {
      case Success(collections) => println("Featured: " + collections.map(_.title).mkString(", "))
      case Failure(error) => println((error))
    }

    unsplash.collections.getCollection(9454911).onComplete {
      case Success(collection) => println("Collection photos: " + collection.title)
      case Failure(error) => println((error))
    }

    unsplash.collections.getRelatedCollections(9454911).onComplete {
      case Success(collections) => println("Related: " + collections.map(_.title).mkString(", "))
      case Failure(error) => println((error))
    }
  }
}
