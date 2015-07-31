import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class sortingReducer extends Reducer<Text,Text,Text,Text> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
		System.out.println("Entered Reducer");
		HashMap<String,Double> hotelname_sentiment = new HashMap<String,Double>();
		String hotelInfo="";
		String hotelName ="";
		Double sentiment =0.0d;
		String[] hotelKeyInfo;
		String hotelId ="";
		
		for(Text value:values){
			hotelInfo = value.toString();
			hotelKeyInfo =hotelInfo.split("\t")[0].split("\\|");
			hotelName = hotelKeyInfo[1];
			hotelId = hotelKeyInfo[0];
			
			sentiment = Double.parseDouble(hotelInfo.split("\t")[1]);
			hotelname_sentiment.put(hotelId+"|"+hotelName, sentiment);
		}
		
		Map<String,Double> sortedSentiment = sortByComparator(hotelname_sentiment);
		List<Entry<String,Double>> list = new LinkedList<Entry<String,Double>>(sortedSentiment.entrySet());
		for(Entry<String,Double>entry:list){
			context.write(new Text(entry.getKey()), new Text(Double.toString(entry.getValue())));
		}
		
		
		
	}

	private Map<String, Double> sortByComparator(
			HashMap<String, Double> hotelname_sentiment) {
		
		List<Entry<String,Double>> list = new LinkedList<Entry<String,Double>>(hotelname_sentiment.entrySet());
		
		Collections.sort(list, new Comparator<Entry<String,Double>>(){
			public int compare(Entry<String,Double>o1,Entry<String,Double>o2){
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		
		Map<String,Double> sortedMap = new LinkedHashMap<String,Double>();
		for(Entry<String,Double> entry:list){
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		
		return sortedMap;
	}
}
