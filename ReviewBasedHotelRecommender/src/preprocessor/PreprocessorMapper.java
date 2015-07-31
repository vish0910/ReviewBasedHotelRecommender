/**
 * 
 */
package preprocessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @author Vishal Doshi and Amruta Nanavaty
 *
 */
public class PreprocessorMapper extends Mapper<Text, Text, Text, Text> {

	@Override
	public void map(Text key, Text value, Context context) throws IOException,
			InterruptedException {
		String val = value.toString();
		Configuration conf = context.getConfiguration();
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(val);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				fs.open(path)));
		String[] newVals = val.split("/Review_Texts/");
		Path newPath = new Path(newVals[0] + "/Processed_Review_Texts/"
				+ newVals[1].split(".dat")[0] + ".txt");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				fs.create(newPath, true)));
		//
		System.out.println("File being processed: " + val);

		String hotelId = newVals[1].split("_")[1].split(".dat")[0];
		br.readLine();// Overall Rating
		String line = br.readLine(); // Avg price
		while (!line.contains("Avg"))
			line = br.readLine();
		System.out.println(line);
		int price;
		if (!(line.contains(("Unknown")) || line.contains(("Unkonwn")))) {
			price = Integer.parseInt((line.split("\\$")[1].replace(",", "")));
		} else {
			price = -1;
		}
		line = br.readLine();
		while (!line.contains("URL"))
			line = br.readLine();
		String[] url = line.split("\\-");
		String hotelName = url[4];
		System.out.println(hotelName);
		String location = url[5].split("\\.")[0]; // html truncated

		context.write(new Text("Location|" + location), new Text("1"));
		line = br.readLine();
		String author = "";
		String content = "";
		while (line != null) {
			if (line.contains("<Author>")) {
				author = line.split("<Author>")[1];
				context.write(new Text("Author|" + author), new Text("1"));
			} else if (line.contains("<Content>")) {
				content = line.split("<Content>")[1];
				context.write(new Text("Review|" + hotelId), new Text("1"));
				bw.write(hotelId + "|" + hotelName + "|" + location + "|"
						+ price + "|" + author + "|" + content + "\n");
			}
			line = br.readLine();
		}
		bw.close();
		br.close();

		//
	}
}
