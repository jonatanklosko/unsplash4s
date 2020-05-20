package unsplash4s

import unsplash4s.entities.User
import io.circe.parser.decode
import unsplash4s.repositories.Photos
import unsplash4s.repositories.Photos.PhotoOrientation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Main {

  def main(args: Array[String]): Unit = {
    val json =
      """
        |{
        |    "id": "xN6aAFeGxVs",
        |    "updated_at": "2020-05-19T09:40:48-04:00",
        |    "username": "jonatanklosko",
        |    "name": "Jonatan Kłosko",
        |    "first_name": "Jonatan",
        |    "last_name": "Kłosko",
        |    "twitter_username": null,
        |    "portfolio_url": null,
        |    "bio": null,
        |    "location": null,
        |    "links": {
        |        "self": "https://api.unsplash.com/users/jonatanklosko",
        |        "html": "https://unsplash.com/@jonatanklosko",
        |        "photos": "https://api.unsplash.com/users/jonatanklosko/photos",
        |        "likes": "https://api.unsplash.com/users/jonatanklosko/likes",
        |        "portfolio": "https://api.unsplash.com/users/jonatanklosko/portfolio",
        |        "following": "https://api.unsplash.com/users/jonatanklosko/following",
        |        "followers": "https://api.unsplash.com/users/jonatanklosko/followers"
        |    },
        |    "profile_image": {
        |        "small": "https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32",
        |        "medium": "https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64",
        |        "large": "https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128"
        |    },
        |    "instagram_username": null,
        |    "total_collections": 0,
        |    "total_likes": 0,
        |    "total_photos": 0,
        |    "accepted_tos": false,
        |    "followed_by_user": false,
        |    "photos": [],
        |    "badge": null,
        |    "tags": {
        |        "custom": [],
        |        "aggregated": []
        |    },
        |    "followers_count": 0,
        |    "following_count": 0,
        |    "allow_messages": true,
        |    "numeric_id": 4693099,
        |    "downloads": 0,
        |    "meta": {
        |        "index": false
        |    }
        |}""".stripMargin

    val user = decode[User](json)
    user match {
      case Left(error) => println(error.getMessage)
      case Right(user) => println(user)
    }

    implicit val client: Client = new Client("q5d_MY49aqBFbJ5rY4KyyN_MHivhhwu4hbcJ3EMJUIk");
//    User.find("jonatanklosko").onComplete {
//      case Success(user) => println(user.name)
//      case Failure(error) => println(error)
//    }
//
//    Photos.all(page = 2, perPage = 5, orderBy = PhotoOrderBy.Popular).onComplete {
//      case Success(body) => println(body.map(_.toString).mkString("\n"))
//      case Failure(error) => println(error)
//    }

//      Photo.find("IlPwZyz-Pl0").onComplete {
//        case Success(photo) => println(photo)
//        case Failure(error) => println(error)
//      }

//    Photos.random(2, query = Some("cat")).onComplete {
//      case Success(body) => println(body.map(_.toString).mkString("\n"))
//      case Failure(error) => println(error)
//    }
    Photos.getUserPhotos("surface", page = 2, perPage = 15, orientation = Some(PhotoOrientation.Portrait)).onComplete {
      case Success(body) => println(body.map(_.toString).mkString("\n"))
      case Failure(error) => println(error)
    }
  }
}
