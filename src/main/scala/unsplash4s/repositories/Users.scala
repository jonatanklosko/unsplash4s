package unsplash4s.repositories

import unsplash4s.HttpClient
import unsplash4s.entities.User
import unsplash4s.Decoders._

import scala.concurrent.Future

class Users(
  httpClient: HttpClient
) {
  def getUser(username: String): Future[User] = {
    httpClient.apiGet[User](s"/users/$username")
  }
}
