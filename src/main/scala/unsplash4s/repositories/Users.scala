package unsplash4s.repositories

import unsplash4s.Client
import unsplash4s.entities.User

import scala.concurrent.Future

object Users {
  def find(username: String)(implicit client: Client): Future[User] = {
    client.request[User](f"/users/$username")
  }
}
