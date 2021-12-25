package satya.practice.mapreduce.usairlines

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

class CancelledCountMapper extends Mapper[LongWritable,Text,Text,LongWritable]{
  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, LongWritable]#Context): Unit = {
    val cols = value.toString.split(",")
    val outKey = new Text(cols(0)+","+cols(1))
    val outValue = new LongWritable(1)
    if(cols.last=="1"){
      context.write(outKey,outValue)
    }
  }
}
