import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;

import model.IranProtests;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;

import java.util.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.awt.event.ItemListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class Application {

	private JFrame frame;
	static List<IranProtests> iranprotests = new ArrayList<>();
	private JTable table;
	private static JLabel lblNewLabel;
	private static JComboBox DateSelect;
	private static JComboBox comboBox;

	private static DefaultTableModel tableModel;
	
	private static void readCSVFile(String filePath) {
		try {
			Scanner scanner = new Scanner(new File(filePath));
			if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				String[] values = line.split(",");

		        IranProtests protests = new IranProtests();

		        // Assuming the date is in the first column
		        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		        Date date;
				try {
					date = dateFormat.parse(values[0]);
			        protests.setDate(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}

		        // Fill in other properties accordingly based on array indexes
		        protests.setDeathTollOfProtestors(Integer.parseInt(values[1]));
		        protests.setNumberOfChildrenKilled(Integer.parseInt(values[2]));
		        protests.setNumberOfMilitarySecurityPersonnelKilled(Integer.parseInt(values[3]));
		        protests.setNumberOfIndividualsArrested(Integer.parseInt(values[4]));
		        protests.setNumberOfDetaineesIdentified(Integer.parseInt(values[5]));
		        protests.setNumberOfStudentsArrested(Integer.parseInt(values[6]));
		        protests.setNumberOfProtests(Integer.parseInt(values[7]));
		        protests.setNumberOfCitiesInvolved(Integer.parseInt(values[8]));
		        protests.setNumberOfUniversitiesInvolved(Integer.parseInt(values[9]));
		        iranprotests.add(protests);
				
			}
			 Collections.sort(iranprotests, Comparator.comparing(IranProtests::getDate));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	 private static Set<Integer> getUniqueYears(List<IranProtests> iranProtestsList) {
	        Set<Integer> uniqueYears = new HashSet<>();

	        for (IranProtests protests : iranProtestsList) {
	            // Extract the year from the date
	            Calendar calendar = Calendar.getInstance();
	            calendar.setTime(protests.getDate());
	            int year = calendar.get(Calendar.YEAR);

	            uniqueYears.add(year);
	        }

	        return uniqueYears;
	 }
	 
	 private static void filterData() {
		 tableModel.setRowCount(0);
		 if(comboBox.getSelectedIndex() == 1) {
			Collections.sort(iranprotests, Comparator.comparing(IranProtests::getDate).reversed());
		}
		else {
			Collections.sort(iranprotests, Comparator.comparing(IranProtests::getDate));
		}
		if(DateSelect.getSelectedIndex() == 0 || DateSelect.getSelectedIndex() == 1) {
			for (IranProtests protest : iranprotests) {
	            Object[] rowData = {
	                    new SimpleDateFormat("MM/dd/yyyy").format(protest.getDate()),
	                    protest.getDeathTollOfProtestors(),
	                    protest.getNumberOfChildrenKilled(),
	                    protest.getNumberOfMilitarySecurityPersonnelKilled(),
	                    protest.getNumberOfIndividualsArrested(),
	                    protest.getNumberOfDetaineesIdentified(),
	                    protest.getNumberOfStudentsArrested(),
	                    protest.getNumberOfProtests(),
	                    protest.getNumberOfCitiesInvolved(),
	                    protest.getNumberOfUniversitiesInvolved()
	                    /* add more data as needed */
	            };
	            tableModel.addRow(rowData);
	        }	
		}
		else {
			String selectedYearString = (String) DateSelect.getSelectedItem();
			int selectedYear = Integer.parseInt(selectedYearString);
			for (IranProtests protests : iranprotests) {

	            Calendar calendar = Calendar.getInstance();
	            calendar.setTime(protests.getDate());
	            int year = calendar.get(Calendar.YEAR);

	            if(selectedYear == year) {
	            	Object[] rowData = {
		                    new SimpleDateFormat("MM/dd/yyyy").format(protests.getDate()),
		                    protests.getDeathTollOfProtestors(),
		                    protests.getNumberOfChildrenKilled(),
		                    protests.getNumberOfMilitarySecurityPersonnelKilled(),
		                    protests.getNumberOfIndividualsArrested(),
		                    protests.getNumberOfDetaineesIdentified(),
		                    protests.getNumberOfStudentsArrested(),
		                    protests.getNumberOfProtests(),
		                    protests.getNumberOfCitiesInvolved(),
		                    protests.getNumberOfUniversitiesInvolved()
		            };
		            tableModel.addRow(rowData);
	            }
	        }
		}
		 
	 }
	 
	 private static void updateImageSize() {
        // Get the current size of the backgroundImgLabel
        int width = lblNewLabel.getWidth();
        int height = lblNewLabel.getHeight();

        // Scale the image to fit the new size
        ImageIcon imgIcon = new ImageIcon("src/bgImg.jpeg");
        Image imgImage = imgIcon.getImage();
        Image imgScale = imgImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledImg = new ImageIcon(imgScale);
        lblNewLabel.setIcon(scaledImg);
    }


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					readCSVFile("src/IranProtests.csv");
					Application window = new Application();
					window.frame.setVisible(true);
					
					updateImageSize();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setAlwaysOnTop(true);
		frame.setBounds(100, 100, 847, 481);
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Convert Set<Date> to String[]
        String[] dates = getUniqueYears(iranprotests).stream()
                .map(year -> Integer.toString(year))
                .toArray(String[]::new);
        
        String[] datesWithAll = new String[dates.length + 2];
        datesWithAll[0] = "Select Year";
        datesWithAll[1] = "ALL";
        
        System.arraycopy(dates, 0, datesWithAll, 2, dates.length);
		
		String[] columnNames = {"Date", "Death Toll",
				"Children Killed",
                "Military Personnel Killed",
                "Individuals Arrested",
                "Detainees Identified",
                "Students Arrested",
                "Number of Protests",
                "Cities Involved",
                "Universities Involved"};

        tableModel = new DefaultTableModel(columnNames, 0);
        for (IranProtests protest : iranprotests) {
            Object[] rowData = {
                    new SimpleDateFormat("MM/dd/yyyy").format(protest.getDate()),
                    protest.getDeathTollOfProtestors(),
                    protest.getNumberOfChildrenKilled(),
                    protest.getNumberOfMilitarySecurityPersonnelKilled(),
                    protest.getNumberOfIndividualsArrested(),
                    protest.getNumberOfDetaineesIdentified(),
                    protest.getNumberOfStudentsArrested(),
                    protest.getNumberOfProtests(),
                    protest.getNumberOfCitiesInvolved(),
                    protest.getNumberOfUniversitiesInvolved()
            };
            tableModel.addRow(rowData);
        }
        JScrollPane scrollPane = new JScrollPane();
		
		table = new JTable(tableModel);
		scrollPane.setViewportView(table);
		
		DateSelect = new JComboBox(datesWithAll);
		DateSelect.setSelectedIndex(0);
		DateSelect.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				filterData();
			}
		});
		DateSelect.setToolTipText("");
		
		
		
		String[] orderByStrings = {"Order By Date ASC","Order By Date DESC"};
		comboBox = new JComboBox(orderByStrings);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				filterData();
			}
		});
		
		lblNewLabel = new JLabel("New label");
		

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(275)
					.addComponent(DateSelect, 0, 130, Short.MAX_VALUE)
					.addGap(40)
					.addComponent(comboBox, 0, 130, Short.MAX_VALUE)
					.addGap(256))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(60)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
					.addGap(150))
				.addGroup(groupLayout.createSequentialGroup()
//					.addContainerGap()
					.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
//					.addContainerGap()
					)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(106)
							.addComponent(DateSelect, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(106)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGap(226)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
							.addGap(48))
						.addGroup(groupLayout.createSequentialGroup()
//							.addContainerGap()
							.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)))
//					.addGap(8)
					)
		);
		frame.getContentPane().setLayout(groupLayout);
		

		frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Update the image size when the frame size changes
                updateImageSize();
            }
        });
		

	}
}
