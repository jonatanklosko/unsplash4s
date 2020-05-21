package examples

import unsplash4s.{Unsplash, UnsplashAppConfig}

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object GetCollectionPhotos {
  def main(args: Array[String]): Unit = {
    val unsplashAppConfig = UnsplashAppConfig(
      applicationAccessKey = "q5d_MY49aqBFbJ5rY4KyyN_MHivhhwu4hbcJ3EMJUIk",
      applicationSecret = Some("OzReyy3mDJn2X2LOJpUdj9NksSNxZcWE0nrr2yovSYY"),
      oauthRedirectUri = Some("urn:ietf:wg:oauth:2.0:oob")
    );
    val unsplash = new Unsplash(unsplashAppConfig)

    unsplash.photos.getCollectionPhotos(9454911).onComplete {
      case Success(photos) => println(photos.map(_.id).mkString(", "))
      case Failure(error) => println((error))
    }
  }
}
