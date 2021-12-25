package satya.practice.mapreduce.usairlines

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Reducer

import scala.collection.JavaConverters._
import java.lang
import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`

class CancelledCounterReducer extends Reducer[Text,LongWritable,Text,LongWritable]{
  override def reduce(key: Text, values: lang.Iterable[LongWritable], context: Reducer[Text, LongWritable, Text, LongWritable]#Context): Unit = {
    val outValue = new LongWritable(values.toList.count(l => true))
    context.write(key,outValue)
  }
}
