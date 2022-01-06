package satya.practice.spark.ny_school_bus
import org.apache.spark.rdd.RDD
import satya.practice.utils.SparkFactory

import java.util.Date
class SchoolBus {}

case class SchoolBusRecord(
    schoolYear: String,
    runType: String,
    busNum: String,
    routeNum: String,
    reason: String,
    occurredOn: Date,
    numberOfStudents: Int
)

object SchoolBus {

  def main(args: Array[String]): Unit = {
    println("args : " + args.mkString(" "))
    val inputPath      = args(0)
    val outputBasePath = args(1)

    val spark = SparkFactory.getOrCreate("School Bus Insights")
    import java.text.SimpleDateFormat
    val dtF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val sc  = spark.sparkContext
    val rdd = sc
      .textFile(inputPath)
      .mapPartitionsWithIndex((idx, iter) =>
        if (idx == 0) iter.drop(1) else iter
      )
      .map(line => {
        val arr = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)

        SchoolBusRecord(
          arr(0),
          arr(1),
          arr(2),
          arr(3),
          arr(4),
          dtF.parse(arr(5)),
          arr(6).toInt
        )

      })

    val mostCommonReason = sc.parallelize(
      rdd
        .map(rec => (rec.reason, 1))
        .foldByKey(0)((x, y) => x + y)
        .takeOrdered(1)(Ordering.by[(String, Int), Int](_._2).reverse)
    )

    mostCommonReason
      .coalesce(1)
      .saveAsTextFile(outputBasePath + "/leastNumAccidents")

    val top5 = sc.parallelize(
      rdd
        .map(rec => {
          (rec.routeNum, 1)
        })
        .foldByKey(0)((x, y) => x + y)
        .takeOrdered(5)(Ordering.by[(String, Int), Int](_._2).reverse)
    )

    top5.coalesce(1).saveAsTextFile(outputBasePath + "/top5")

    val yearlyInBusIncidentsCount =
      rdd
        .filter(rec => rec.numberOfStudents > 0)
        .map(rec => { (rec.schoolYear, 1) })
        .foldByKey(0)((x, y) => x + y)

    yearlyInBusIncidentsCount
      .coalesce(1)
      .saveAsTextFile(outputBasePath + "/yearlyInBusIncidentsCount")

    val yearlyNotInBusIncidents = rdd
      .filter(rec => rec.numberOfStudents == 0)
      .map(rec => { (rec.schoolYear, 1) })
      .foldByKey(0)((x, y) => x + y)

    yearlyNotInBusIncidents
      .coalesce(1)
      .saveAsTextFile(outputBasePath + "/yearlyNotInBusIncidentsCount")

    val leastNumAccidents = sc.parallelize(
      rdd
        .filter(rec => rec.reason.equalsIgnoreCase("Accident"))
        .map(rec => {
          (rec.schoolYear, 1)
        })
        .foldByKey(0)((x, y) => x + y)
        .takeOrdered(1)(Ordering.by[(String, Int), Int](_._2))
    )

    leastNumAccidents
      .coalesce(1)
      .saveAsTextFile(outputBasePath + "/leastNumAccidents")

  }

}
