package utils

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._
import org.apache.spark.sql.SQLContext

case class WordCount(word: String, count: Int)

object SparkSQL {

  def simpleSparkSQLApp {
    val logFile = "public/data/README.md" // Should be some file on your system
    val driverHost = "localhost"
    val conf = new SparkConf(false) // skip loading external settings
      .setMaster("local[4]") // run locally with enough threads
      .setAppName("firstSparkApp")
      .set("spark.logConf", "true")
      .set("spark.driver.host", s"$driverHost")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 4).cache()
    val words = logData.flatMap(_.split(" "))
    
    val sqlContext = new SQLContext(sc)
    
    import sqlContext._
    
    val wordCount = words.map(word => (word,1)).reduceByKey(_+_).map(wc => WordCount(wc._1, wc._2))
    wordCount.registerAsTable("wordCount")
    
    val moreThanTenCounters = wordCount.where('count > 10).select('word)
    
    println("Words occuring more than 10 times are : ")
    moreThanTenCounters.map(mttc => "Word : " + mttc(0)).collect().foreach(println)
    
  }

}