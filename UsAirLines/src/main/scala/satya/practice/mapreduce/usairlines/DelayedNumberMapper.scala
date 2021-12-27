package satya.practice.mapreduce.usairlines

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

class DelayedNumberMapper
    extends Mapper[LongWritable, Text, Text, LongWritable] {
  override def map(
      key: LongWritable,
      value: Text,
      context: Mapper[LongWritable, Text, Text, LongWritable]#Context
  ): Unit = {
    val cols    = value.toString.split(",")
    val airline = cols(2)
    try {
      val delay = cols(8).toInt
      if (delay > 0) {
        context.write(new Text(airline), new LongWritable(1))
      }
    } catch {
      case e: NumberFormatException => {}
    }
  }
}
