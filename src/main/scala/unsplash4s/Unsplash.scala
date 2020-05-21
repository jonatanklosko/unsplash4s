package unsplash4s

import unsplash4s.repositories.{OAuth, Photos, Users}

class Unsplash(
  appConfig: UnsplashAppConfig,
  accessToken: Option[String] = None
) {
  val httpClient = new HttpClient(appConfig, accessToken)

  lazy val oauth = new OAuth(httpClient, appConfig)
  lazy val photos = new Photos(httpClient)
  lazy val users = new Users(httpClient)
}
