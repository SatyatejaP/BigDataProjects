package satya.practice.mapreduce.usairlines

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

import java.io.InputStreamReader
import scala.collection.mutable

class CancelledDivertedAirlinesMapper
    extends Mapper[LongWritable, Text, Text, LongWritable] {
  var airlinesMap: mutable.Map[String, String] = mutable.Map()

  override def setup(
      context: Mapper[LongWritable, Text, Text, LongWritable]#Context
  ): Unit = {
    import java.io.BufferedReader
    val p = context.getCacheFiles

    for (x <- p) {
      val fs     = FileSystem.get(context.getConfiguration)
      val br     = new BufferedReader(new InputStreamReader(fs.open(new Path(x))))
      var inpstr = br.readLine
      while (inpstr != null) {
        val k = inpstr.split(",")(0).toUpperCase.trim
        val v = inpstr.split(",")(1).toUpperCase.trim
        airlinesMap += (k -> v)
        inpstr = br.readLine
      }
    }
  }

  override def map(
      key: LongWritable,
      value: Text,
      context: Mapper[LongWritable, Text, Text, LongWritable]#Context
  ): Unit = {
    val kout = new Text()
    val vout = new LongWritable(1)
    val cols = value.toString.split(",")
    if (cols(9) == "1" || cols(10) == "1") {
      kout.set(airlinesMap(cols(2)))
      context.write(kout, vout)
    }
  }
}
