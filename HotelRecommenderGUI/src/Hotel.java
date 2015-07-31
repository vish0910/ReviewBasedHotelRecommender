

/**
 * @author Vishal Doshi and Amruta Nanavaty
 *
 */
public class Hotel {
	String hotelName;
	String hotelId;
	double score;
	
	public Hotel(String hotelName, String hotelId, double sentiment) {
		// TODO Auto-generated constructor stub
		this.hotelName =  hotelName;
		this.hotelId = hotelId;
		this.score = sentiment;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

}
