import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "PlaySandbox"
  val appVersion      = "0.2"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    "net.vz.mongodb.jackson" %% "play-mongo-jackson-mapper" % "1.1.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
