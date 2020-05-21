package unsplash4s.entities

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
