package unsplash4s.entities

import java.time.Instant

case class User(id: String,
                username: String,
                name: String,
                firstName: String,
                lastName: String,
                /* ? */
                bio: Option[String],
                location: Option[String],
                twitterUsername: Option[String],
                instagramUsername: Option[String],

                totalCollections: Int,
                totalLikes: Int,
                totalPhotos: Int,
                acceptedTos: Boolean) {

}
