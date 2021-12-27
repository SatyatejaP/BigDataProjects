package satya.practice.mapreduce.usairlines

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat

import java.net.URI

class DelayedNumber {}
object DelayedNumber {
  def main(args: Array[String]): Unit = {
    val inputPath  = new Path(args(0))
    val outputPath = new Path(args(1))
    val conf       = new Configuration()
    val j          = new Job(conf)
    j.setJar("UsAirLines.jar")
    j.setJobName("Delayed Number per airline")
    j.setMapperClass(classOf[DelayedNumberMapper])
    j.setReducerClass(classOf[CounterReducer])
    j.setMapOutputKeyClass(classOf[Text])
    j.setMapOutputValueClass(classOf[LongWritable])
    FileInputFormat.addInputPath(j, inputPath)
    FileOutputFormat.setOutputPath(j, outputPath)
    import org.apache.hadoop.fs.FileSystem
    val uri = new URI(args(1))

    val fs = FileSystem.get(uri, conf)

    val x = fs.delete(new Path(uri), true)

    j.waitForCompletion(true)

  }
}
