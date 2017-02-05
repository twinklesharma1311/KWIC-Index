package se1;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.List;
import java.util.Collections;
import javax.swing.JFrame;
import javax.swing.JApplet;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CyberMiner_Applet extends JFrame implements ActionListener {
	Socket white_socks;
	PrintWriter outWriter;
	BufferedReader inReader;
	String searchString = "";
	
	// The KWIC generating panel and etc.
	JPanel kwicPanel = new JPanel();
	JPanel kwicLeft = new JPanel();
	JPanel kwicRight = new JPanel();
	JPanel resultsPanel = new JPanel();
	JTextArea shiftedLines = new JTextArea();
	JPanel URLpanel = new JPanel();
	JPanel descriptionPanel = new JPanel();
	JCheckBox initial = new JCheckBox("Initial Entries");
	JTextField URL = new JTextField();
	JTextField description = new JTextField();
	JTextArea output = new JTextArea();
	JScrollPane scroll;
	JButton shiftNstore = new JButton("Submit");
	
	// The Search Panel and etc.
	JPanel search = new JPanel();
	JPanel searchCard = new JPanel();
	JPanel outputCard = new JPanel();
	JPanel removeCard = new JPanel();
	SearchArea searchBox = new SearchArea();
	JPanel searchSize = new JPanel();
	JScrollPane roll = new JScrollPane();
	JScrollPane rattle = new JScrollPane();
	JEditorPane searchResults = new JEditorPane();
	JTextField resultsPerPage = new JTextField("5", 3);
	JTextField page = new JTextField("1", 3);
	JButton go = new JButton("Go");
	JPanel resultsControl = new JPanel();
	JButton searchButton = new JButton("Search");
	JButton back = new JButton("Back");
	JButton remove = new JButton("Remove");
	JPanel commitPanel = new JPanel();
	JTextField urlToRemove = new JTextField(15);
	JButton commitRemove = new JButton("Remove");
	// The main menu Panel and etc.
	JTabbedPane mainMenu = new JTabbedPane();
	private CircularShift cs = new CircularShift();
	private Alphabetizer ab = new Alphabetizer();
	private Filter ft = new Filter();
	private TreeSet<String> words;
	private String[] searchWords;

	public CyberMiner_Applet() {
		words = new TreeSet<String>();
		searchWords = new String[0];
		setTitle("Cyber Miner");
		setSize(500, 500);
		// KWIC Initialization
		kwicLeft.setLayout(new GridLayout(3, 1));
		URL.setColumns(15);
		URLpanel.add(URL);
		URLpanel.setPreferredSize(new Dimension(250, 40));
		URLpanel.setBorder(BorderFactory.createTitledBorder("URL"));
		description.setColumns(15);
		descriptionPanel.add(description);
		descriptionPanel.setPreferredSize(new Dimension(250, 40));
		descriptionPanel.setBorder(BorderFactory.createTitledBorder("Description"));
		kwicLeft.add(URLpanel);
		kwicLeft.add(descriptionPanel);
		shiftNstore.setPreferredSize(new Dimension(100, 30));
		shiftNstore.addActionListener(this);
		JPanel sizePanel = new JPanel();
		sizePanel.setPreferredSize(new Dimension(380, 30));
		sizePanel.add(shiftNstore);
		sizePanel.add(initial, BorderLayout.EAST);
		kwicLeft.add(sizePanel);
		output.setRows(29);
		output.setColumns(20);
		output.setEditable(false);
		rattle = new JScrollPane(searchResults);
		scroll = new JScrollPane(output);
		kwicRight.add(resultsPanel);
		resultsPanel.setPreferredSize(new Dimension(250,500));
		resultsPanel.setBorder(BorderFactory.createTitledBorder("Results"));
		shiftedLines.setColumns(20);
		//shiftedLines.setText("Hi\nHi\nHiHi\nHi\nHiHi\nHi\nHiHi\nHi\nHiHi\nHi\nHiHi\nHi\nHi");
		resultsPanel.add(shiftedLines);
		kwicRight.add(scroll);
		kwicPanel.setLayout(new GridLayout(1, 2));
		kwicPanel.add(kwicLeft);
		kwicPanel.add(kwicRight);
		mainMenu.addTab("KWIC Entries", kwicPanel);
		
		// Search Initialization
		//searchResults.setRows(23);
		//searchResults.setColumns(40);
		searchResults.setPreferredSize(new Dimension (470,370));
		searchResults.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
		searchResults.setEditable(false);
		searchResults.setOpaque(true);
		searchResults.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e)
			{
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
				{
					if(Desktop.isDesktopSupported())
					{
						try
						{
							Desktop.getDesktop().browse(e.getURL().toURI());
						}
						catch(IOException ex)
						{
							ex.printStackTrace();
						}
						catch(URISyntaxException ex)
						{
							ex.printStackTrace();
						}
					}
				}
			}
		});
		searchCard.add(searchBox);
		searchButton.setPreferredSize(new Dimension(100, 30));
		searchButton.addActionListener(this);
		searchSize.setPreferredSize(new Dimension(250, 40));
		searchSize.add(searchButton);
		searchCard.add(searchSize);
		searchCard.setBorder(BorderFactory.createTitledBorder("Search"));
		outputCard.add(rattle);
		back.addActionListener(this);
		resultsControl.add(back);
		resultsControl.add(new JLabel("Results Per Page"));
		resultsControl.add(resultsPerPage);
		resultsControl.add(new JLabel("Page"));
		resultsControl.add(page);
		resultsControl.add(go);
		resultsControl.add(remove);
		go.addActionListener(this);
		remove.addActionListener(this);
		commitRemove.setPreferredSize(new Dimension(100,30));
		commitPanel.add(commitRemove);
		removeCard.add(urlToRemove);
		removeCard.add(commitPanel, BorderLayout.SOUTH);
		outputCard.add(resultsControl, BorderLayout.SOUTH);
		outputCard.setBorder(BorderFactory.createTitledBorder("Results"));
		removeCard.setBorder(BorderFactory.createTitledBorder("Remove Broken Links"));
		search.setLayout(new CardLayout());
		commitRemove.addActionListener(this);
		search.add(searchCard);
		search.add(outputCard);
		search.add(removeCard);
		mainMenu.addTab("Search", search);
		// Final initialization stuff
		add(mainMenu);
		validate();
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == shiftNstore) {
			String url = URL.getText(); System.out.println("URL: " + url);
			String orgLine = description.getText(); System.out.println("orgLine: " + orgLine);
			String temp = orgLine;
			String[] tempWords = temp.split(" ");
			for (String s : tempWords)
				words.add(s);
			List<String> tempList = new ArrayList<String>();
			tempList.addAll(words);
			searchBox.setSearchTerms(tempList);
			ArrayList<String> toDataBase = ft.inputList(ab.inputList(cs.inputLine(temp)));
			
			String finalResults = "";
			for(String s : toDataBase)
				finalResults += s + "\n";
			shiftedLines.setText(finalResults);
			
			try {
				Socket white_socks = new Socket("localhost", 1234);
				PrintWriter outWriter = new PrintWriter(white_socks.getOutputStream(), true);
				BufferedReader inReader = new BufferedReader(new InputStreamReader(white_socks.getInputStream()));
				
				if (initial.isSelected())
					outWriter.println("1");
				else
					outWriter.println("2");
				outWriter.println(url);
				outWriter.println(orgLine);
				String csLines = "";
				for (int i = 0; i < toDataBase.size(); i++) {
					if (i != toDataBase.size() - 1)
						csLines += toDataBase.get(i) + "/";
					else
						csLines += toDataBase.get(i);
				}
				System.out.println("CSLines: " + csLines);
				outWriter.println(csLines);
				outWriter.close();
				inReader.close();
				white_socks.close();
			} catch (IOException ex) {
				System.err.println("Could not resolve host, 'localhost' on port 1492");
			}

		} else if (e.getSource() == searchButton) {
			try {
				Socket white_socks = new Socket("localhost", 1492);
				PrintWriter outWriter = new PrintWriter(white_socks.getOutputStream(), true);
				BufferedReader inReader = new BufferedReader(new InputStreamReader(white_socks.getInputStream()));
				outWriter.println("3");
				searchString =  searchBox.getText();
				System.out.println("SearchString: " + searchString);
				outWriter.println(searchString);
				
				//Waiting for result
				String result = inReader.readLine();
				System.out.println("Result from server: " + result);
				
				//Splitting for formatted result
				String newResult = "";
				searchWords = result.split(";");
				ArrayList<String> sortTheWords = new ArrayList<String>(1);
				for(String shenanigans : searchWords)
				{
					sortTheWords.add(shenanigans);
				}
				Collections.sort(sortTheWords);
				for(int z = 0; z < sortTheWords.size(); z++)
				{
					searchWords[z] = sortTheWords.get(z);
				}
				int numPerPage = Integer.parseInt(resultsPerPage.getText());
				int pageNum = Integer.parseInt(page.getText());
				if(numPerPage < 1)
					numPerPage = 1;
				if(pageNum < 1)
					pageNum = 1;
				int initialValue = numPerPage * (pageNum-1);
				for(int j = initialValue; j < initialValue + numPerPage; j++)
				{
						if(j < searchWords.length)
						{
							String words1[] = searchWords[j].split(",");
							newResult += "<a href=\"" + words1[1] + "\">" + words1[1] + "</a>" + "<br>" + words1[0] + "<br><br>";
						}
						else
						{
							newResult += " ";
						}
				}
				searchResults.setText(newResult);
				/**
				 * Read from inReader the results separated by | and separate,
				 * then format based on numPerPage and pageNum
				 */
				outWriter.close();
				inReader.close();
				white_socks.close();
			} catch (IOException ex) {
				System.err.println("Could not resolve host, 'localhost' on port 1492");
			}
			((CardLayout) (search.getLayout())).next(search);
			
		} else if (e.getSource() == go) {
			int numPerPage = Integer.parseInt(resultsPerPage.getText());
			int pageNum = Integer.parseInt(page.getText());
			if(numPerPage < 1)
				numPerPage = 1;
			if(pageNum < 1)
				pageNum = 1;
			String newResult = "";
			int initialValue = numPerPage * (pageNum-1);
			for(int j = initialValue; j < initialValue + numPerPage; j++)
			{
					if(j < searchWords.length)
					{
						String words1[] = searchWords[j].split(",");
						newResult += "<a href=\""  + words1[1] + "\">" + words1[1] + "</a>" + "<br>" + words1[0] + "<br><br>";
					}
					else
					{
						newResult += " ";
					}
			}
			searchResults.setText(newResult);
		}
		else if (e.getSource() == back) 
		{
			((CardLayout) (search.getLayout())).previous(search);
		}
		else if (e.getSource() == remove)
		{
			((CardLayout)(search.getLayout())).next(search);
		}
		else if (e.getSource() == commitRemove)
		{
			try {
				Socket white_socks = new Socket("localhost", 1492);
				PrintWriter outWriter = new PrintWriter(white_socks.getOutputStream(), true);
				BufferedReader inReader = new BufferedReader(new InputStreamReader(white_socks.getInputStream()));
				
				outWriter.println("4");
				String urlToDelete = urlToRemove.getText();
				outWriter.println(urlToDelete);
				outWriter.close();
				inReader.close();
				white_socks.close();
			} catch (IOException ex) {
				System.err.println("Could not resolve host, 'localhost' on port 1492");
			}

			
			((CardLayout)(search.getLayout())).first(search);
		}
	}

	public static void main(String[] args) {
		CyberMiner_Applet e = new CyberMiner_Applet();
	}
}