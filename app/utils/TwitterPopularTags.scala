package utils

import org.apache.spark.streaming.{ Seconds, StreamingContext }
import StreamingContext._
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.twitter._
import play.api.Play
import org.apache.spark.SparkConf
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import scala.collection.immutable.HashMap

object TwitterPopularTags {

  def TwitterStreamUtil {

    val data = new HashMap[String, Int]
    // Twitter Authentication credentials
    val consumerKey = Play.current.configuration.getString("consumer_key").get
    val consumerSecret = Play.current.configuration.getString("consumer_secret").get
    val accessToken = Play.current.configuration.getString("access_token").get
    val accessTokenSecret = Play.current.configuration.getString("access_token_secret").get

    // Authorising with your Twitter Application credentials
    val twitter = new TwitterFactory().getInstance()
    twitter.setOAuthConsumer(consumerKey, consumerSecret)
    twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret))
    
    val driverPort = 8080
    val driverHost = "localhost"
    val conf = new SparkConf(false) // skip loading external settings
      .setMaster("local[4]") // run locally with enough threads
      .setAppName("firstSparkApp")
      .set("spark.logConf", "true")
      .set("spark.driver.port", s"$driverPort")
      .set("spark.driver.host", s"$driverHost")
      .set("spark.akka.logLifecycleEvents", "true")

    val ssc = new StreamingContext(conf, Seconds(2))
    val filters = Seq("fifa")
    val stream = TwitterUtils.createStream(ssc, Option(twitter.getAuthorization()), filters)

    val hashTags = stream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))
    
    val topCounts60 = hashTags.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(60))
      .map { case (topic, count) => (count, topic) }
      .transform(_.sortByKey(false))
          
    val topCounts10 = hashTags.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(10))
      .map { case (topic, count) => (count, topic) }
      .transform(_.sortByKey(false))

    // Print popular hashtags
    topCounts60.foreachRDD(rdd => {
      val topList = rdd.take(5)
      println("\nPopular topics in last 60 seconds (%s total):".format(rdd.count()))
      topList.foreach { case (count, tag) => println("%s (%s tweets)".format(tag, count)) }
    })

    topCounts10.foreachRDD(rdd => {
      val topList = rdd.take(5)
      println("\nPopular topics in last 10 seconds (%s total):".format(rdd.count()))
      topList.foreach { case (count, tag) => println("%s (%s tweets)".format(tag, count)) }
    })

    ssc.start()
    ssc.awaitTermination()
  }

}
