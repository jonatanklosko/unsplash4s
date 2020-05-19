package unsplash4s.entities

import java.time.Instant

case class Photo(id: String,
                 createdAt: Instant,
                 updatedAt: Instant,
                 promotedAt: Option[Instant],
                 width: Int,
                 height: Int,
                 color: String,
                 likes: Int,
                 likedByUser: Boolean,
                 description: Option[String],
                 altDescription: Option[String],
                 urls: PhotoUrls,
                 links: PhotoLinks, /* Do we actually need it? (easy to compute) */
                 user: User
                 /* TODO: all fields */) {

}
