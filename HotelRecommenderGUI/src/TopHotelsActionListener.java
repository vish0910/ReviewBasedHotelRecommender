import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 */

/**
 * @author Vish
 *
 */
public class TopHotelsActionListener implements ActionListener {

	int index = 0;
	public TopHotelsActionListener(int i) {
		// TODO Auto-generated constructor stub
		super();
		index = i;
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
//		HotelRecommenderGUI.populateReviews(index);
	}

}
