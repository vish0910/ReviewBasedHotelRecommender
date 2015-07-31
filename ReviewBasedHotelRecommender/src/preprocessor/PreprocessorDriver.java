package preprocessor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.net.URISyntaxException;

import org.apache.commons.httpclient.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @author Vishal Doshi and Amruta Nanavaty
 *
 */
public class PreprocessorDriver extends Configured implements Tool {

	public int getListOfFiles(String[] args, Configuration conf)
			throws IOException, URISyntaxException {
		// 2. Get the instance of the HDFS
		FileSystem hdfs = FileSystem.get(conf);
		// 3. Get the metadata of the desired directory
		FileStatus[] fileStatus = hdfs.listStatus(new Path("Review_Texts"));
		// 4. Using FileUtil, getting the Paths for all the FileStatus
		Path[] paths = FileUtil.stat2Paths(fileStatus);
		// 5. Iterate through the directory and display the files in it
		Path newGraphFile = new Path("input/inputFileList.txt");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				hdfs.create(newGraphFile, true)));
		CharSequence cs = ".dat";
		int count = 0;
		for (Path path : paths) {
			if (path.toString().contains(cs)) {
				System.out.println(path);
				count++;
				try {
					bw.write(count + "\t" + path + "\n");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		bw.close();
		return count;
	}

	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// if(args.length!=2){
		// System.out.println("Invalid Parameters\nEnter Input file path and beta value");
		// return -1;
		// }
		Configuration conf = getConf(); // THIS IS THE CORRECTWAY
		conf.set(
				"mapreduce.input.keyvaluelinerecordreader.key.value.separator",
				"\t");
		// conf.set("beta", args[1]);
		int countN = getListOfFiles(args, conf);
		// conf.set("numberOfNodes", countN+"");
		String ip, op;
		Job job = new Job(conf, "Preprocessor");

		// Driver Class
		job.setJarByClass(PreprocessorDriver.class);

		// Setting Mapper and Reducer Class
		job.setMapperClass(PreprocessorMapper.class);
		job.setReducerClass(PreprocessorReducer.class);

		// Setting Mapper Key and Value output type
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		// Setting final Key and Value Type
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setInputFormatClass(KeyValueTextInputFormat.class);

		// Set number of Reducers for job1
		job.setNumReduceTasks(4);

		// Creating file-paths

		ip = "input/inputFileList.txt";

		op = "output/out";

		// Setting the paths
		FileInputFormat.setInputPaths(job, new Path(ip));
		FileOutputFormat.setOutputPath(job, new Path(op));

		// Running the job.
		job.waitForCompletion(true);

		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int res = ToolRunner.run(new Configuration(), new PreprocessorDriver(),
				args);
		System.exit(res);

	}

}
