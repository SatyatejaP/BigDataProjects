package satya.practice.mapreduce.usairlines

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Reducer

import scala.collection.JavaConversions._
import java.lang

class CounterReducer extends Reducer[Text, LongWritable, Text, LongWritable] {
  override def reduce(
      key: Text,
      values: lang.Iterable[LongWritable],
      context: Reducer[Text, LongWritable, Text, LongWritable]#Context
  ): Unit = {
    var count: Long = 0
    values.toList.foreach(v => count += v.get())
    context.write(key, new LongWritable(count))
  }
}
