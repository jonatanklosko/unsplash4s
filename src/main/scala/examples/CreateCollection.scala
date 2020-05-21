package examples

import unsplash4s.entities.AccessToken.Scope
import unsplash4s.{Unsplash, UnsplashAppConfig}

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn

object CreateCollection {
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

    unsplash.oauth.getAccessToken(code)
      .flatMap(token => {
        println(s"Obtained access token: $token")
        val userUnsplash = new Unsplash(unsplashAppConfig, Some(token.accessToken))
        print("Enter new collection name: ")
        val name = StdIn.readLine()
        userUnsplash.collections.createCollection(name)
          .flatMap(collection => {
            val photoId = "xulIYVIbYIc"
            userUnsplash.collections.addPhotoToCollection(collection.id, photoId)
          })
      })
      .onComplete {
        case Success(_) => println("Collection created!")
        case Failure(error) => println(error)
      }
  }
}
