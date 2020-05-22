package examples

import unsplash4s.{Unsplash, UnsplashAppConfig}
import unsplash4s.entities.AccessToken.Scope

import scala.concurrent.Await
import scala.io.StdIn
import scala.concurrent.duration.Duration

object CollectionComposer {
  def main(args: Array[String]): Unit = {
    val unsplashAppConfig = UnsplashAppConfig(
      applicationAccessKey = "q5d_MY49aqBFbJ5rY4KyyN_MHivhhwu4hbcJ3EMJUIk",
      applicationSecret = Some("OzReyy3mDJn2X2LOJpUdj9NksSNxZcWE0nrr2yovSYY"),
      oauthRedirectUri = Some("urn:ietf:wg:oauth:2.0:oob")
    );
    val unsplash = new Unsplash(unsplashAppConfig)
    val oauthUrl = unsplash.oauth.authorizationUrl(scope = Set(Scope.Public, Scope.ReadCollections, Scope.WriteCollections))
    println(s"Please get a code from: $oauthUrl")
    print("Enter the code: ")
    val code = StdIn.readLine()

    val token = Await.result(unsplash.oauth.getAccessToken(code), Duration.Inf)
    println(s"Obtained access token: $token")
    val userUnsplash = new Unsplash(unsplashAppConfig, Some(token.accessToken))
    print("Enter new collection name: ")
    val name = StdIn.readLine()
    val collection = Await.result(userUnsplash.collections.createCollection(name), Duration.Inf)
    println(s"Collection ${collection.title} created!")

    val randomPhotos = Await.result(userUnsplash.photos.getRandomPhotos(30), Duration.Inf)
    for (photo <- randomPhotos) {
      println(s"Photo: ${photo.urls.full}")
      print("Do you like this photo? [yN] ")
      val answer = StdIn.readLine()
      if (answer.equalsIgnoreCase("y")) {
        Await.result(userUnsplash.collections.addPhotoToCollection(collection.id, photo.id), Duration.Inf)
        println("Added photo to your collection!")
      }
    }
  }
}
