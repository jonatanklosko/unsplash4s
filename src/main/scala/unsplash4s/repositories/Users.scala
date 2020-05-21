package unsplash4s.repositories

import unsplash4s.Client
import unsplash4s.entities.User
import unsplash4s.Decoders._

import scala.concurrent.Future

object Users {
  def getUser(username: String)(implicit client: Client): Future[User] = {
    client.request[User](s"/users/$username")
  }
}
