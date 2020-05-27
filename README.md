# Unsplash4s

[Unsplash](https://unsplash.com) API wrapper for Scala.
Check out the [Unsplash API Docs](https://unsplash.com/documentation) for more details.

## Set up

You need to create an Unsplash account and [register your application](https://unsplash.com/documentation#getting-started)
in order to obtain an access key.

## Examples

### Basic usage

To query public data create a new Unsplash client with
your application access key, and you are free to go.

```scala
import unsplash4s.entities.Photo
import unsplash4s.{Unsplash, UnsplashAppConfig}
import scala.concurrent.{Future,Await}
import scala.concurrent.duration.Duration

/* Create configuration with your application data. */
val unsplashAppConfig = UnsplashAppConfig(
  applicationAccessKey = "<your-access-key>"
);

/* Create an Unsplash client. */
val unsplash = new Unsplash(unsplashAppConfig)

/* Query the Unsplash API using the client. */
val photosF: Future[Seq[Photo]] = unsplash.photos.getPhotos()
val photos = Await.result(photosF, Duration.Inf)

for (photo <- photos) println(f"Photo URL: ${photo.urls.full}")
```

### Authorized usage

When it comes to authorization Unsplash implements OAuth,
so you can perform actions on user's behalf once you obtain an access token.

```scala
import unsplash4s.{Unsplash,UnsplashAppConfig}
import unsplash4s.entities.AccessToken.Scope
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/* Create configuration including your application secret and OAuth redirect URI. */
val unsplashAppConfig = UnsplashAppConfig(
  applicationAccessKey = "<your-access-key>",
  applicationSecret = Some("<your-secret-key>"),
  oauthRedirectUri = Some("<your-oauth-redirect-uri>")
);

/* Create an Unsplash client. */
val unsplash = new Unsplash(unsplashAppConfig)

/* Get OAuth URL to redirect the user to. Make sure to include the desired scopes. */
val oauthUrl = unsplash.oauth.authorizationUrl(scope = Set(Scope.Public, Scope.WriteLikes))

/* Exchange the code you get back from Unsplash for an access token.
   This usually takes place in the OAuth callback on your server. */
val code = "<the code>"
val token = Await.result(unsplash.oauth.getAccessToken(code), Duration.Inf)

/* Create a new Unsplash client, passing the newly obtained token this time. */
val userUnsplash = new Unsplash(unsplashAppConfig, Some(token.accessToken))

/* Make authorized requests on behalf of the user. */
userUnsplash.photos.likePhoto("brooklyn99")
```

### A complete example

This is a simple application showcasing the most important parts of this package.
It authorizes an Unsplash user using OAuth, creates a collection on his behalf,
asks for a topic and then presents him with random photos matching this topic,
allowing them to add the interesting photos to the new collection.

```scala
import unsplash4s.{Unsplash,UnsplashAppConfig}
import unsplash4s.entities.AccessToken.Scope

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.StdIn

object CollectionComposer {
  def main(args: Array[String]): Unit = {
    val unsplashAppConfig = UnsplashAppConfig(
      applicationAccessKey = "<your-access-key>",
      applicationSecret = Some("<your-secret-key>"),
      oauthRedirectUri = Some("<your-oauth-redirect-uri>")
    );
    val unsplash = new Unsplash(unsplashAppConfig)
    val oauthUrl = unsplash.oauth.authorizationUrl(
      scope = Set(Scope.Public, Scope.ReadCollections, Scope.WriteCollections)
    )
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
```

Developed by:
- Jonatan Kłosko, [@jonatanklosko](https://github.com/jonatanklosko)
- Oliwia Masiarek, [@omasiarek](https://github.com/omasiarek)
