package examples

import unsplash4s.{Unsplash, UnsplashAppConfig}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Basic {
  def main(args: Array[String]): Unit = {
    /* Create configuration with your application data. */
    val unsplashAppConfig: UnsplashAppConfig = UnsplashAppConfig(
      applicationAccessKey = "q5d_MY49aqBFbJ5rY4KyyN_MHivhhwu4hbcJ3EMJUIk",
      applicationSecret = Some("OzReyy3mDJn2X2LOJpUdj9NksSNxZcWE0nrr2yovSYY"),
      oauthRedirectUri = Some("urn:ietf:wg:oauth:2.0:oob")
    );
    /* Create an Unsplash client. */
    val unsplash = new Unsplash(unsplashAppConfig)
    /* Query the Unsplash API using the client. */
    unsplash.photos.getPhotos(perPage = 5).onComplete {
      case Success(photos) => println(photos.map(_.urls.full).mkString("\n"))
      case Failure(error) => println(error)
    }
  }
}
