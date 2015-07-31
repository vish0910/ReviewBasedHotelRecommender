import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class sortingMapper extends Mapper<Object,Text,Text,Text> {

	public Text hotelLocation = new Text("");

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException{
		
		System.out.println("Entered Mapper");
		Configuration conf = context.getConfiguration();
	String userSpecifiedLocation=	conf.get("location");
		
		String val = value.toString();
		String location ="";
		if(!val.isEmpty()){
			location = val.split("\t")[0].split("\\|")[2];			
		
		if(location.equalsIgnoreCase(userSpecifiedLocation)){
			hotelLocation.set(location);
			context.write(hotelLocation, new Text(val));
		}
		
		}
	}
}
