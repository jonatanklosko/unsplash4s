package examples

import unsplash4s.UnsplashAppConfig

object Examples {
  val unsplashAppConfig: UnsplashAppConfig = UnsplashAppConfig(
    applicationAccessKey = "q5d_MY49aqBFbJ5rY4KyyN_MHivhhwu4hbcJ3EMJUIk",
    applicationSecret = Some("OzReyy3mDJn2X2LOJpUdj9NksSNxZcWE0nrr2yovSYY"),
    oauthRedirectUri = Some("urn:ietf:wg:oauth:2.0:oob")
  );
}
