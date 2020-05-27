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
)

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
)

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
    )
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

![](images/demo.gif)

## API Docs

### `case class UnsplashAppConfig`

Holds configuration of an application registered on Unsplash.

#### Attributes

| Attribute | Type | Description |
| --- | --- | --- |
| `applicationAccessKey` | `String` | The access key assigned by Unsplash. |
| `applicationSecret` | `Option[String]` | The secret key assigned by Unsplash. Necessary when using OAuth. |
| `oauthRedirectUri` | `Option[String]` | The OAuth redirect URL of the application. Necessary when using OAuth. |
| `apiUrl` | `String` | The base URL of Unsplash API. *Default: `https://api.unsplash.com`* |
| `oauthUrl` | `String` | The base URL of Unsplash OAuth. *Default: `https://unsplash.com/oauth`* |
| `apiVersion` | `String` | The API version to target. *Default: `v1`* |

#### Example

```scala
val unsplashAppConfig = UnsplashAppConfig(
  applicationAccessKey = "<your-access-key>",
  applicationSecret = Some("<your-secret-key>"),
  oauthRedirectUri = Some("<your-oauth-redirect-uri>")
)
```

### `class Unsplash`

Represents an Unsplash client. That's the top-level class you're going to use to make all requests.

#### Attributes

| Attribute | Type | Description |
| --- | --- | --- |
| `appConfig` | `UnsplashAppConfig` | The configuration of an Unsplash application. |
| `accessToken` | `Option[String]` | User access token obtained through OAuth. |

#### Properties

| Property | Type | Description |
| --- | --- | --- |
| `oauth` | `OAuth` | Wrapper for OAuth endpoints. |
| `photos` | `Photos` | Wrapper for photo endpoints. |
| `users` | `Users` | Wrapper for user endpoints. |
| `collections` | `Collections` | Wrapper for collection endpoints. |

#### Example

```scala
val unsplashAppConfig = UnsplashAppConfig(applicationAccessKey = "<your-access-key>")
val unsplash = new Unsplash(unsplashAppConfig)

val photosF = unsplash.photos.searchPhotos("cats")
val usersF = unsplash.users.searchUsers("john")
val collectionsF = unsplash.users.searchCollections("cats")
```

### `class Photos`

Wrapper for photo endpoints. Use through an `Unsplash` object.

#### Methods

##### `getPhoto`

Retrieves a single photo.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `id` | `String` | Id of the photo to fetch. |

Returns `Future[Photo]`.

**Example**

```scala
val photosF = unsplash.photos.getPhoto("xyz123")
```

##### `getPhotos`

Gets a single page from the list of all photos.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `page` | `Int` | Page number to retrieve. *Default: `1`.* |
| `perPage` | `Int` | Number of items per page, max 30. *Default: `10`.* |
| `orderBy` | `PhotoOrderBy` | How to sort the photos. *Default: `PhotoOrderBy.Latest`.* |

Returns `Future[Seq[Photo]]`.

**Example**

```scala
val photosF = unsplash.photos.getPhotos(page = 2, perPage = 30, orderBy = PhotoOrderBy.Popular)
```

##### `getRandomPhotos`

Retrieves a single random photo, given optional filters.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `count` | `Int` | The number of photos to retrieve. *Default: `1`.* |
| `query` | `Option[String]` | Limit selection to photos matching a search term. *Default: `None`.* |
| `featured` | `Option[Boolean]` | Limit selection to featured photos. *Default: `None`.* |
| `username` | `Option[String]` | Limit selection to a single user. *Default: `None`.* |
| `collectionIds` | `Option[Seq[Int]]` | Public collection ID(‘s) to filter selection. *Default: `None`.* |
| `orientation` | `Option[PhotoOrientation]` | Filter by photo orientation. *Default: `None`.* |
| `contentFilter` | `Option[ContentFilter]` | Limit results by [content safety](https://unsplash.com/documentation#content-safety). *Default: `None`.* |

Returns `Future[Seq[Photo]]`.

**Example**

```scala
val photosF = unsplash.photos.getRandomPhotos(15, query = "cats", featured = Some(true))
```

##### `getUserPhotos`

Gets a list of photos uploaded by a user.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `username` | `String` | Username to retrieve photos for. |
| `page` | `Int` | Page number to retrieve. *Default: `1`.* |
| `perPage` | `Int` | Number of items per page, max 30. *Default: `10`.* |
| `orderBy` | `PhotoOrderBy` | How to sort the photos. *Default: `PhotoOrderBy.Latest`.* |
| `orientation` | `Option[PhotoOrientation]` | 	Filter by photo orientation. *Default: `None`.* |

Returns `Future[Seq[Photo]]`.

**Example**

```scala
val photosF = unsplash.photos.getUserPhotos("sherlock", orientation = PhotoOrientation.Landscape)
```

##### `getCollectionPhotos`

Retrieves a collection's photos.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `collectionId` | `Int` | Collection ID to retrieve photos for. |
| `page` | `Int` | Page number to retrieve. *Default: `1`.* |
| `perPage` | `Int` | Number of items per page, max 30. *Default: `10`.* |
| `orderBy` | `PhotoOrderBy` | How to sort the photos. *Default: `PhotoOrderBy.Latest`.* |
| `orientation` | `Option[PhotoOrientation]` | 	Filter by photo orientation. *Default: `None`.* |

Returns `Future[Seq[Photo]]`.

**Example**

```scala
val photosF = unsplash.photos.getCollectionPhotos(10, orientation = PhotoOrientation.Landscape)
```

##### `searchPhotos`

Gets a single page of photo results for a query.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `query` | `String` | Search terms. |
| `page` | `Int` | Page number to retrieve. *Default: `1`.* |
| `perPage` | `Int` | Number of items per page, max 30. *Default: `10`.* |
| `collectionIds` | `Option[Seq[Int]]` | Public collection ID(‘s) to filter selection. *Default: `None`.* |
| `orientation` | `Option[PhotoOrientation]` | Filter by photo orientation. *Default: `None`.* |
| `contentFilter` | `Option[ContentFilter]` | Limit results by [content safety](https://unsplash.com/documentation#content-safety). *Default: `None`.* |

Returns `Future[SearchResult[Photo]]`.

**Example**

```scala
val photosF = unsplash.photos.searchPhotos("cats", page = 2, perPage = 30, orientation = PhotoOrderBy.Portrait)
```

##### `likePhoto`

Likes a photo on behalf of the logged-in user.
This requires the Unsplash client to have an OAuth access token obtained with `Scope.WriteLikes` scope.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `id` | `String` | The photo's ID. |

Returns `Future[Unit]`.

**Example**

```scala
unsplash.photos.likePhoto("xyz123")
```

##### `unlikePhoto`

Removes a user’s like of a photo.
This requires the Unsplash client to have an OAuth access token obtained with `Scope.WriteLikes` scope.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `id` | `String` | The photo's ID. |

Returns `Future[Unit]`.

**Example**

```scala
unsplash.photos.unlikePhoto("xyz123")
```

##### `triggerPhotoDownload`

Triggers photo download to track that action. See [Unsplash Docs](https://unsplash.com/documentation#track-a-photo-download) for details.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `id` | `String` | The photo's ID. |

Returns `Future[Unit]`.

**Example**

```scala
unsplash.photos.triggerPhotoDownload("xyz123")
```

##### `getPhotoStatistics`

Retrieves total number of downloads, views and likes of a single photo, as well as the historical breakdown of these stats in a 30 day timeframe.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `id` | `String` | The photo's ID. |

Returns `Future[Statistic]`.

**Example**

```scala
val statisticF = unsplash.photos.getPhotoStatistics("xyz123")
```

### `class Users`

Wrapper for user endpoints. Use through an `Unsplash` object.

#### Methods

##### `getUser`

Retrieves public details of the given user.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `username` | `String` | The user's username. |

Returns `Future[User]`.

**Example**

```scala
val userF = unsplash.users.getUser("sherlock")
```

##### `searchUsers`

Gets a single page of user results for a query.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `query` | `String` | Search terms. |
| `page` | `Int` | Page number to retrieve. *Default: `1`.* |
| `perPage` | `Int` | Number of items per page, max 30. *Default: `10`.* |

Returns `Future[Seq[User]]`.

**Example**

```scala
val usersF = unsplash.users.searchUsers("john")
```

##### `getUserStatistics`

Retrieve the consolidated number of downloads, views and likes of all user's photos, as well as the historical breakdown of these stats in 30 day timeframe.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `username` | `String` | The user's username. |

Returns `Future[Statistic]`.

**Example**

```scala
val statisticF = unsplash.users.getUserStatistics("sherlock")
```


### `class Collections`

Wrapper for collection endpoints. Use through an `Unsplash` object.

#### Methods

##### `getCollection`

Retrieves a single collection.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `id` | `Int` | Id of the collection to fetch. |

Returns `Future[Collection]`.

**Example**

```scala
val collectionF = unsplash.collections.getCollection(111)
```

##### `getCollections`

Gets a single page from the list of all collections.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `page` | `Int` | Page number to retrieve. *Default: `1`.* |
| `perPage` | `Int` | Number of items per page, max 30. *Default: `10`.* |

Returns `Future[Seq[Collection]]`.

**Example**

```scala
val collectionsF = unsplash.collections.getCollections(page = 2, perPage = 30)
```

##### `getFeaturedCollections`

Gets a single page from the list of featured collections.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `page` | `Int` | Page number to retrieve. *Default: `1`.* |
| `perPage` | `Int` | Number of items per page, max 30. *Default: `10`.* |

Returns `Future[Seq[Collection]]`.

**Example**

```scala
val collectionsF = unsplash.collections.getFeaturedCollections(page = 2, perPage = 30)
```

##### `searchCollections`

Gets a single page of user results for a query.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `query` | `String` | Search terms. |
| `page` | `Int` | Page number to retrieve. *Default: `1`.* |
| `perPage` | `Int` | Number of items per page, max 30. *Default: `10`.* |

Returns `Future[Seq[Collection]]`.

**Example**

```scala
val collectionsF = unsplash.collections.searchCollections("cats")
```

##### `getRelatedCollections`

Retrieves a list of collections related to the given one.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `id` | `Int` | The collection's id. |

Returns `Future[Seq[Collection]]`.

**Example**

```scala
val collectionsF = unsplash.collections.getRelatedCollections(111)
```

##### `createCollection`

Create a new collection on behalf of the logged-in user.
This requires the Unsplash client to have an OAuth access token obtained with `Scope.WriteCollections` scope.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `title` | `String` | The title of the collection. |
| `description` | `Option[String]` | The description of the collection. *Default: `None`.* |
| `private` | `Option[Boolean]` | Whether to make this collection private. *Default: `false`.* |

Returns `Future[Collection]`.

**Example**

```scala
val collectionF = unsplash.collections.createCollection("Best", Some("My loved pictures."), true)
```

##### `deleteCollection`

Delete a collection on behalf of the logged-in user.
This requires the Unsplash client to have an OAuth access token obtained with `Scope.WriteCollections` scope.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `id` | `Int` | The collection's id. |

Returns `Future[Unit]`.

**Example**

```scala
unsplash.collections.deleteCollection(10144)
```

##### `addPhotoToCollection`

Add a photo to collection on behalf of the logged-in user.
This requires the Unsplash client to have an OAuth access token obtained with `Scope.WriteCollections` scope.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `id` | `Int` | The collection's ID. |
| `photoId` | `String` | The photo's ID. |

Returns `Future[Unit]`.

**Example**

```scala
unsplash.collections.addPhotoToCollection(10414, "xyz123")
```

##### `removePhotoFromCollection`

Remove a photo from collection on behalf of the logged-in user.
This requires the Unsplash client to have an OAuth access token obtained with `Scope.WriteCollections` scope.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `id` | `Int` | The collection's ID. |
| `photoId` | `String` | The photo's ID. |

Returns `Future[Unit]`.

**Example**

```scala
unsplash.collections.removePhotoFromCollection(10414, "xyz123")
```

### `class OAuth`

Wrapper for OAuth endpoints. Use through an `Unsplash` object.

#### Methods

##### `authorizationUrl`

Get OAuth authorization URL to redirect your app's user to.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `scope` | `Set[Scope]` | The permissions to ask the user for. |

Returns `String`.

**Example**

```scala
unsplash.oauth.authorizationUrl(Set(Scope.Public, Scope.ReadCollections, Scope.WriteCollections))
```

##### `getAccessToken`

Exchange code and application secret for user OAuth token.

**Arguments**

| Argument | Type | Description |
| --- | --- | --- |
| `code` | `String` | OAuth code obtained from Unsplash. |

Returns `Future[AccessToken]`.

**Example**

```scala
val accessTokenF = unsplash.oauth.getAccessToken("xyz123xyz123xyz123xyz123")
```

## Contributors

Developed by:
- Jonatan Kłosko, [@jonatanklosko](https://github.com/jonatanklosko)
- Oliwia Masiarek, [@omasiarek](https://github.com/omasiarek)
