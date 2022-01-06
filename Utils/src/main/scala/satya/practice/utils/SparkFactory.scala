package satya.practice.utils

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkFactory {
  var spark: Option[SparkSession] = None
  // if extraConfString for spark properties  is being passed then should contain following format:
  //extraConfString="key1:val1;key2:val2"
  def getOrCreate(
      appName: String,
      extraConfString: String = ""
  ): SparkSession = {
    spark match {
      case Some(x) => x
      case None =>
        var extraConf: Map[String, String] = Map.empty

        if (extraConfString != "") {
          extraConf = extraConfString
            .split(";")
            .map(_.split(":"))
            .map { case Array(k, v) => (k, v) }
            .toMap
        }
        val conf = new SparkConf()
          .setAll(extraConf)
          .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        val sparkSession = SparkSession
          .builder()
          .appName(appName)
          .config(conf)
          .getOrCreate()

        spark = Some(sparkSession)
        sparkSession
    }
  }
}
