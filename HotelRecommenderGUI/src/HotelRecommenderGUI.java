import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Spliterator;


public class HotelRecommenderGUI {

	private JFrame frame;
	public static BufferedReader br;
	private static DefaultListModel<String> listModel;
	private static JList<String> listReviews;
	private static String tester ;
	private static HashMap<String,Integer> hm;
	private static int numberOfHotels = 5;
	public static JButton[] btnTopHotels = new JButton[numberOfHotels];
	public static String[] listOfLocations = {"Phoenix_Arizona","Seattle_Washington", "Chicago_Illinois", "SanFrancisco_California", "LosAngles_California"};
	public static JComboBox<String> cbDestination, cbTopHotels;
	public static JRadioButton rdbtnOverall, rdbtnFood, rdbtnRoom;
	public static HashMap<String,String> nameid;
	public static JScrollPane scrollPane;
	private static double[] scores;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		populateDictionary();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HotelRecommenderGUI window = new HotelRecommenderGUI();
					window.frame.setVisible(true);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	


	public static void populateDictionary(){
		tester = ": accessed";
		try {
			hm = new HashMap<String, Integer>();
			br = new BufferedReader(new FileReader("input/posAdjOnly.txt"));
			String line = br.readLine();
			while(line!=null){
				hm.put(line, 1);
				line = br.readLine();
			}
			
			br.close();
			
			//Reading negAdjOnly
			br = new BufferedReader(new FileReader("input/negAdjOnly.txt"));
			line = br.readLine();
			while(line!=null){
				hm.put(line, 0);
				line = br.readLine();
			}
			br.close();
			
			//Reading negAdjOnly
			br = new BufferedReader(new FileReader("input/foodNounsOnly.txt"));
			line = br.readLine();
			while(line!=null){
				hm.put(line, 2);
				line = br.readLine();
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * Create the application.
	 */
	public HotelRecommenderGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(135, 206, 250));
		frame.setBackground(Color.WHITE);
//		frame.setBounds(0, 0, 720, 750);
		frame.setSize(700, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Hotel Recommender based on User Reviews");
		
		JLabel lblDestination = new JLabel("Destination:");
		lblDestination.setBounds(53, 16, 87, 16);
		frame.getContentPane().add(lblDestination);
		
		cbDestination = new JComboBox<String>();
		for(int l = 0; l<listOfLocations.length;l++){
			cbDestination.addItem(listOfLocations[l]);
		}
		cbDestination.setBounds(195, 12, 280, 27);
		frame.getContentPane().add(cbDestination);
		
		JLabel lblParameter = new JLabel("Parameter:");
		lblParameter.setBounds(53, 48, 87, 16);
		frame.getContentPane().add(lblParameter);
		
		rdbtnOverall = new JRadioButton("Overall");
		rdbtnOverall.setBounds(195, 44, 76, 23);
		frame.getContentPane().add(rdbtnOverall);
		
		rdbtnFood = new JRadioButton("Food");
		rdbtnFood.setBounds(300, 44, 76, 23);
		frame.getContentPane().add(rdbtnFood);
		
		rdbtnRoom = new JRadioButton("Room");
		rdbtnRoom.setBounds(400, 44, 86, 23);
		frame.getContentPane().add(rdbtnRoom);
		
		//Group the radio buttons.
	    ButtonGroup rdGroup = new ButtonGroup();
		rdGroup.add(rdbtnOverall);
		rdGroup.add(rdbtnFood);
		rdGroup.add(rdbtnRoom);
		
		rdbtnFood.setSelected(true);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchTopHotels();
			}
		});
		btnSearch.setBounds(489, 16, 123, 39);
		frame.getContentPane().add(btnSearch);
		
		cbTopHotels = new JComboBox<String>();
		cbTopHotels.setBounds(195, 79, 280, 27);
		frame.getContentPane().add(cbTopHotels);
		
		JButton btnGetReviews = new JButton("Get Reviews");
		btnGetReviews.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cbTopHotels.getSelectedItem()!=null)
					try {
						populateReviews((String)cbTopHotels.getSelectedItem());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		btnGetReviews.setBounds(489, 67, 123, 39);
		frame.getContentPane().add(btnGetReviews);
		
//		
//		//Specifying button coordinates and size
//		int x_co=10; int y_co = 70; int btnwidth =270; int btnheight = 60;
//		for(int i = 0;i<btnTopHotels.length;i++){
//			btnTopHotels[i] = new JButton("HotelArray "+i);
//			btnTopHotels[i].addActionListener(new TopHotelsActionListener(i));
//			btnTopHotels[i].setBounds(x_co, y_co, btnwidth, btnheight);
//			btnTopHotels[i].setBorder(new LineBorder(Color.BLACK));
//			frame.getContentPane().add(btnTopHotels[i]);
//			y_co +=60; 
//		}
//		
		
		// COMMENT FROM HERE TO =======>
//		JButton btnHotel1 = new JButton("Hotel 1");
//		btnHotel1.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String temp = btnHotel1.getText();
//				populateReviews(temp);
//			}
//		});
//		
//		btnHotel1.setBounds(10, 70, 270, 60);
//		frame.getContentPane().add(btnHotel1);
//		
//		JButton btnHotel2 = new JButton("Hotel 2");
//		btnHotel2.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String temp = btnHotel2.getText();
//				populateReviews(temp);
//
//			}
//		});
//		btnHotel2.setBounds(10, 140, 270, 60);
//		frame.getContentPane().add(btnHotel2);
//		
//		JButton btnHotel3 = new JButton("Hotel 3");
//		btnHotel3.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String temp = btnHotel3.getText();
//				populateReviews(temp);
//
//			}
//		});
//		btnHotel3.setBounds(10, 210, 270, 60);
//		frame.getContentPane().add(btnHotel3);
//		
//		JButton btnHotel4 = new JButton("Hotel 4");
//		btnHotel4.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String temp = btnHotel4.getText();
//				populateReviews(temp);
//
//			}
//		});
//		btnHotel4.setBounds(10, 280, 270, 60);
//		frame.getContentPane().add(btnHotel4);
//		
//		JButton btnHotel5 = new JButton("Hotel 5");
//		btnHotel5.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String temp = btnHotel5.getText();
//				populateReviews(temp);
//
//			}
//		});
//		btnHotel5.setBounds(10, 350, 270, 60);
//		frame.getContentPane().add(btnHotel5);
		//COMMENT TILL HERE =========>
		
		
		listModel = new DefaultListModel<String>();
		listReviews = new JList<String>(listModel);
//		listReviews.setVisibleRowCount(10);
		
		listReviews.setBounds(15, 120, 670, 570);
		listReviews.setCellRenderer(new MyCellRenderer()); //add
//		scrollPane = new JScrollPane(listReviews);
//		scrollPane = new JScrollPane();
//		frame.getContentPane().add(scrollPane);
		
		JScrollPane scrollPane_1 = new JScrollPane(listReviews);
		scrollPane_1.setBounds(10, 120, 680, 580);
		frame.getContentPane().add(scrollPane_1);
		
		JLabel lblTopHotels = new JLabel("Recommended Hotels:");
		lblTopHotels.setBounds(53, 83, 145, 16);
		frame.getContentPane().add(lblTopHotels);
//		scrollPane.setVisible(true);

	}
	
	public static void searchTopHotels(){
		cbTopHotels.removeAllItems();
		String location = (String)cbDestination.getSelectedItem();
		String aspect = "";
		String[] args = new String[2];
		
		if(rdbtnFood.isSelected()){
			aspect = rdbtnFood.getText();
		}
//		args[0] = location;
//		args[1] = aspect;
//		listModel.addElement(args[0]);
//		listModel.addElement(args[1]);
		//MAKE CALL TO DRIVER
		sortingDriver sd = new sortingDriver();
		boolean result =false;
		try {
			result = sd.driver(location, aspect);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished"+result);
		
		try {
			br = new BufferedReader(new FileReader("sortedResults/part-r-00000"));
			String line;
			String[] values;
			String[] keyValue;
			nameid = new HashMap<String,String>();
			scores = new double[numberOfHotels];
			for(int i = 0;i<numberOfHotels; i++){
				line = br.readLine();
				if(line == null){
					break;
				}
				values = line.split("\t");
				keyValue = values[0].split("\\|");
				System.out.println(values[0]);
				System.out.println(keyValue[0]);
				System.out.println(keyValue[1]);
				nameid.put(keyValue[1], keyValue[0]+"");
				cbTopHotels.addItem((i+1)+":"+keyValue[1]);
				System.out.println("Value :"+values[1]);
				scores[i] = Double.parseDouble(values[1]);
			}
			br.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Call 
	}
	
	
	//To Clear the list
	public static void clearList(){
		listModel.clear();
	}
	//To populate the list
	public static void populateReviews(String s) throws IOException{
//		for(int j=0;j<numberOfHotels;j++){
//			if(j==index){
//				btnTopHotels[j].setEnabled(false);
//				btnTopHotels[j].setBackground(Color.BLUE);;
//			}
//			else{
//				btnTopHotels[j].setEnabled(true);
//				c
//			}
//		}
		String filename = "Reviews_nd_Sentiment/"+cbDestination.getSelectedItem()+"/hotel_"+nameid.get(s.split(":")[1])+".txt";
		String[] highlightColor = {"#F08080","#00FF7F","#F0E68C"};
		int color;
		clearList();
//		double score = Double.parseDouble();
		listModel.addElement("<span style='background-color:'><b>Score:"+scores[(Integer.parseInt(s.split(":")[0])-1)]+"</b></span>");
		listModel.addElement("<b>Accessing: "+filename+tester+"</b>");
		String reviewIn="";
		int count =1;
		br = new BufferedReader(new FileReader(filename));
		reviewIn = br.readLine();
		while (reviewIn != null) {
			String reviewOut = "";
			String[] tokens = reviewIn.split("\\|")[0].split(" ");
			for (int i = 0; i < tokens.length; i++) {
				if (hm.containsKey(tokens[i])) {
					color = hm.get(tokens[i]);
					reviewOut += "<span style='background-color:"
							+ highlightColor[color] + "'>" + tokens[i]
							+ "</span>";
				} else {
					reviewOut += tokens[i];
					
				}
				reviewOut += " ";
			}

			if (!reviewOut.isEmpty()){
				listModel.addElement("<b>"+count+":</b> "+reviewOut);
				count++;
				}
			reviewIn = br.readLine();
		}
//		String reviewIn = "Hello how quiet is it ? or was it noisy ? can you please tell me? Tell me something that is funny neat clean and what else!";
//		String reviewOut = "";
//		String[] tokens = reviewIn.split(" ");
//		for(int i = 0; i< tokens.length;i++){
//			if(hm.containsKey(tokens[i])){
//				color = hm.get(tokens[i]);
//				reviewOut+="<span style='background-color:"+highlightColor[color]+"'>"+tokens[i]+"</span>";
//			}
//			else{
//				reviewOut+=tokens[i];
//			}
//			reviewOut+=" ";
//		}
//		
////		if(hm.containsKey("quiet"))
////			reviewIn+="<span style='background-color:#00FF7F'>present</span>";
//		if(!reviewOut.isEmpty())
//			listModel.addElement(reviewOut);
	}
	
	//--------
//	public static void populateReviews(int index){
//		for(int j=0;j<numberOfHotels;j++){
//			if(j==index){
//				btnTopHotels[j].setEnabled(false);
//				btnTopHotels[j].setBackground(Color.BLUE);;
//			}
//			else{
//				btnTopHotels[j].setEnabled(true);
//			}
//		}
//		String whichHotel = btnTopHotels[index].getText();
//		String[] highlightColor = {"#F08080","#00FF7F"};
//		int color;
//		clearList();
//		listModel.addElement(whichHotel+tester);
//		String reviewIn = "Hello how quiet is it ? or was it noisy ? can you please tell me?";
//		String reviewOut = "";
//		String[] tokens = reviewIn.split(" ");
//		for(int i = 0; i< tokens.length;i++){
//			if(hm.containsKey(tokens[i])){
//				color = hm.get(tokens[i]);
//				reviewOut+="<span style='background-color:"+highlightColor[color]+"'>"+tokens[i]+"</span>";
//			}
//			else{
//				reviewOut+=tokens[i];
//			}
//			reviewOut+=" ";
//		}
//		
////		if(hm.containsKey("quiet"))
////			reviewIn+="<span style='background-color:#00FF7F'>present</span>";
//		if(!reviewOut.isEmpty())
//			listModel.addElement(reviewOut);
//	}
	
	//-------
	public class MyCellRenderer extends DefaultListCellRenderer
    {
        final JPanel p = new JPanel(new BorderLayout());
//        final JPanel IconPanel = new JPanel(new BorderLayout());
//        final JLabel l = new JLabel("icon"); //<-- this will be an icon instead of a text
        final JLabel lt = new JLabel();
        String pre = "<html><body style='width: 500px;'>";
//        String pre = "<html><body>";


        MyCellRenderer() {
            //icon
//            IconPanel.add(l, BorderLayout.NORTH);
//            p.add(IconPanel, BorderLayout.WEST);

            p.add(lt, BorderLayout.CENTER);
            //text
        }

        @Override
        public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean hasFocus)
        {
            final String text = (String) value;
            lt.setText(pre+ text);
            lt.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,Color.BLACK)); //added

            return p;
        }
    }
}
