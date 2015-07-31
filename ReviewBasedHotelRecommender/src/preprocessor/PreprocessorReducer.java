/**
 * 
 */
package preprocessor;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @author Vishal Doshi and Amruta Nanavaty
 *
 */
public class PreprocessorReducer extends Reducer<Text, Text, Text, Text> {
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		int count = 0;
		for (Text val : values) {
			count += Integer.parseInt(val.toString());
		}
		context.write(key, new Text(count + ""));
	}

}
