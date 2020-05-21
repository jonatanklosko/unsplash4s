package unsplash4s.entities

import java.time.Instant

import unsplash4s.entities.AccessToken.Scope.Scope

case class AccessToken(
  accessToken: String,
  tokenType: String,
  scope: Seq[Scope],
  createdAt: Instant
)

object AccessToken {
  object Scope extends Enumeration {
    type Scope = Value
    val Public = Value("public")
    val ReadUser = Value("read_user")
    val WriteUser = Value("write_user")
    val ReadPhotos = Value("read_photos")
    val WritePhotos = Value("write_photos")
    val WriteLikes = Value("write_likes")
    val WriteFollowers = Value("write_followers")
    val ReadCollections = Value("read_collections")
    val WriteCollections = Value("write_collections")
  }
}
