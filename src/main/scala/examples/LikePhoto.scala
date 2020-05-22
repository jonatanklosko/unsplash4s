package examples

import unsplash4s.Unsplash
import unsplash4s.entities.AccessToken.Scope

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn
import scala.util.{Failure, Success}

object LikePhoto {
  def main(args: Array[String]): Unit = {
    val unsplash = new Unsplash(Examples.unsplashAppConfig)

    val oauthUrl = unsplash.oauth.authorizationUrl(scope = Set(Scope.Public, Scope.WriteLikes))
    println(s"Please get a code from: $oauthUrl")
    print("Enter the code: ")
    val code = StdIn.readLine()

    unsplash.oauth.getAccessToken(code)
      .flatMap(token => {
        println(s"Obtained access token: $token")
        val userUnsplash = new Unsplash(Examples.unsplashAppConfig, Some(token.accessToken))
        val photoId = "xulIYVIbYIc"
        userUnsplash.photos.likePhoto(photoId)
      })
      .onComplete {
        case Success(_) => println("Photo liked!")
        case Failure(error) => println(error)
      }
  }
}
