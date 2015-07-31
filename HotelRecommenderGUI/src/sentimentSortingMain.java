import java.io.IOException;


public class sentimentSortingMain {

	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		
		sortingDriver sd = new sortingDriver();
		boolean result =false;
		result = sd.driver("Phoenix_Arizona", "Food");
		System.out.println("Finished"+result);

	}

}
