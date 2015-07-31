import java.io.IOException;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;



public class sortingDriver {

/*public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		
		
		boolean result =false;
		result = driver("Phoenix_Arizona", "Food");
		System.out.println("Finished"+result);

	} */
	public boolean driver(String location, String aspect) throws IOException, ClassNotFoundException, InterruptedException{
		
//	String location ="Phoenix_Arizona";
//	String aspect ="Food";
		
		Configuration conf = new Configuration();
		System.out.println("Entered Driver");
		conf.set("aspectNames", "Overall,Food,Room,Staff");
		conf.set("location", location);
		conf.set("aspect", aspect);
		Job job2 = new Job(conf,"sortingDriver");
		job2.setJarByClass(sortingDriver.class);
		job2.setMapperClass(sortingMapper.class);
		job2.setReducerClass(sortingReducer.class);
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);
		FileInputFormat.setInputPaths(job2, new Path("/home/amruta/Desktop/ids561Project/SentimentSorting/sentimentResults"));
		FileOutputFormat.setOutputPath(job2, new Path("/home/amruta/Desktop/ids561Project/SentimentSorting/sortedResults"));		
		boolean flag =	job2.waitForCompletion(true);
		System.out.println("Sorting finished");
		return flag;
		
		
		
	}
	
}
