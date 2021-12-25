package satya.practice.mapreduce.usairlines

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat

import java.net.URI

class CancelledCounter{}

object CancelledCounter {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    val j = new Job(conf, "Cancelled Flights count")
    j.setJar("UsAirLines.jar")
    j.setJobName("Cancelled Counter")
    j.setMapperClass(classOf[CancelledCountMapper])
    j.setReducerClass(classOf[CancelledCountReducer])
    j.setMapOutputKeyClass(classOf[Text])
    j.setMapOutputValueClass(classOf[LongWritable])
    FileInputFormat.addInputPath(j, new Path(args(0)))
    FileOutputFormat.setOutputPath(j, new Path(args(1)))
    import org.apache.hadoop.fs.FileSystem
    val uri = new URI(args(1))

    val fs = FileSystem.get(uri, conf)

    val x = fs.delete(new Path(uri), true)


    j.waitForCompletion(true)
  }
}
