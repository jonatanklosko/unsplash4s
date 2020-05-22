package examples

import unsplash4s.Unsplash
import unsplash4s.entities.AccessToken.Scope

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.StdIn

object CollectionComposer {
  def main(args: Array[String]): Unit = {
    val unsplash = new Unsplash(Examples.unsplashAppConfig)
    val oauthUrl = unsplash.oauth.authorizationUrl(scope = Set(Scope.Public, Scope.ReadCollections, Scope.WriteCollections))
    println(s"Please get a code from: $oauthUrl")
    print("Enter the code: ")
    val code = StdIn.readLine()

    val token = Await.result(unsplash.oauth.getAccessToken(code), Duration.Inf)
    println(s"Obtained access token: $token")
    val userUnsplash = new Unsplash(Examples.unsplashAppConfig, Some(token.accessToken))
    print("Enter new collection name: ")
    val name = StdIn.readLine()
    val collection = Await.result(userUnsplash.collections.createCollection(name), Duration.Inf)
    println(s"Collection ${collection.title} created!")

    print("Enter a topic you're interested in: ")
    val search = StdIn.readLine()
    val randomPhotos = Await.result(
      userUnsplash.photos.getRandomPhotos(30, query = Some(search), featured = Some(true)),
      Duration.Inf
    )
    for (photo <- randomPhotos) {
      println(s"Photo: ${photo.urls.regular}")
      print("Do you like this photo? [yN] ")
      val answer = StdIn.readLine()
      if (answer.equalsIgnoreCase("y")) {
        Await.result(userUnsplash.collections.addPhotoToCollection(collection.id, photo.id), Duration.Inf)
        println("Added the photo to your collection!")
      }
    }
  }
}
