package satya.practice.spark.ny_school_bus

import scopt.OptionParser

case class SchoolBusProperties(inputPath: String = "", sparkConf: String = "")
object SchoolBusPropertiesParser {
  val parser: OptionParser[SchoolBusProperties] =
    new OptionParser[SchoolBusProperties]("School bus options") {

      head("School Bus Properties")

      opt[String]("inputPath")
        .text("Input for School Bus data in csv or text format")
        .required()
        .action((v, c) => c.copy(inputPath = v))

      opt[String]("sparkConf")
        .text("spark conf string in k1:v1;k2:v2...kn:vn format")
        .optional()
        .action((v, c) => c.copy(inputPath = v))
    }
}
