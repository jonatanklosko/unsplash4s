package unsplash4s.entities

import io.circe.{Decoder, HCursor}

case class User(
  id: String,
  username: String,
  name: String,
  firstName: String,
  lastName: Option[String],
  bio: Option[String],
  location: Option[String],
  portfolioUrl: Option[String],
  twitterUsername: Option[String],
  instagramUsername: Option[String],
  profileImage: ProfileImage,
  totalCollections: Int,
  totalLikes: Int,
  totalPhotos: Int,
  acceptedTos: Boolean
)

object User {
  implicit val userDecoder: Decoder[User] = (c: HCursor) => {
    for {
      id <- c.downField("id").as[String]
      username <- c.downField("username").as[String]
      name <- c.downField("name").as[String]
      firstName <- c.downField("first_name").as[String]
      lastName <- c.downField("last_name").as[Option[String]]
      bio <- c.downField("bio").as[Option[String]]
      location <- c.downField("location").as[Option[String]]
      portfolioUrl <- c.downField("portfolio_url").as[Option[String]]
      twitterUsername <- c.downField("twitter_username").as[Option[String]]
      instagramUsername <- c.downField("instagram_username").as[Option[String]]
      profileImage <- c.downField("profile_image").as[ProfileImage]
      totalCollections <- c.downField("total_collections").as[Int]
      totalLikes <- c.downField("total_likes").as[Int]
      totalPhotos <- c.downField("total_photos").as[Int]
      acceptedTos <- c.downField("accepted_tos").as[Boolean]
    } yield {
      User(
        id = id,
        username = username,
        name = name,
        firstName = firstName,
        lastName = lastName,
        bio = bio,
        location = location,
        portfolioUrl  = portfolioUrl,
        twitterUsername = twitterUsername,
        instagramUsername = instagramUsername,
        profileImage = profileImage,
        totalCollections = totalCollections,
        totalLikes = totalLikes,
        totalPhotos = totalPhotos,
        acceptedTos = acceptedTos
      )
    }
  }
}
