package se1;

import java.net.*;
import java.util.Scanner;
import java.sql.SQLException;
import java.io.*;

public class KWIC_Thread extends Thread {
	private Socket socks = null;
	private DataAccess da;
	DataOutputStream os = null;

	public KWIC_Thread(Socket black_socks) {
		super("KWIC_Thread");
		socks = black_socks;
		da = new DataAccess();
	}

	public void run() {
		System.out.println("Thread has started");
		try {
			PrintWriter out = new PrintWriter(socks.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socks.getInputStream()));
			int code;
			String inputCode, url, searchTerms, orgLine;

			while ((inputCode = in.readLine()) != null) {
				code = Integer.parseInt(inputCode);
				System.out.println("Code is: " + code);
				
				//Comparing the code against various cases
				switch (code) {
				case 0:
					PrintWriter writer = new PrintWriter("words.txt", "UTF-8");
					String autoComplete = in.readLine();
					writer.println(autoComplete);
					writer.close();
					break;
				case 1:
					url = in.readLine();
					orgLine = in.readLine();
					searchTerms = in.readLine(); // KWIC initial input
					System.out.println("SearchTerms: " + searchTerms);
					/*
					 * Search terms is CS shifted lines, separated by "/",
					 * separate them then input into database
					 */
					try {
						da.insertURL(url, orgLine, searchTerms, 1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
					
				case 2:
					url = in.readLine();
					orgLine = in.readLine();
					searchTerms = in.readLine(); // KWIC input non-initial
					System.out.println("URL: " + url);
					System.out.println("SearchTerms: " + searchTerms);
					/*
					 * Search terms as above, separate them and input into
					 * database
					 */
					try {
						da.addUrl(url, orgLine, searchTerms, 1);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case 3:
					/* Output formatted: URL | non-CS line | */
					String result = null;
					String searchString = in.readLine().toLowerCase();
					System.out.println("Searching for string " + searchString);
					
					//Default search
					if(!searchString.contains(" + ") && !searchString.contains(" - ") && !searchString.contains(" & ")){
						String words[] = searchString.split(" ");
						try {
							result = da.defaultSearch(words);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					//Multiple ands
					else if(searchString.contains(" & ")){
						String words[] = searchString.split(" & ");
						System.out.println("\nMultipleAnds Case");
						System.out.println("The and words are: ");
						for(String s : words)
							System.out.println(s);
						
						try {
							result = da.multipleAnds(words);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					//One not
					else if(searchString.contains(" - ")){
						String words[] = searchString.split(" - ");
						System.out.println("\nOne Not Case");
						System.out.println("The not words are: ");
						for(String s : words)
							System.out.println(s);
						
						try {
							result = da.multipleNots(words);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
					//Printing to client applet
					if(result.equals(""))
						result = "null,null";
					os = new DataOutputStream(socks.getOutputStream());
					os.writeBytes(result + "\n");
					break; // Search request
				case 4:
					String urlToDelete = in.readLine().toLowerCase();
					System.out.println("Url To delete: " + urlToDelete);
					
					try {
						da.deleteURL(urlToDelete);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*
					 * Removal request: URL contains the url to be removed from
					 * database
					 */
					break;
				case 5:
					try
					{
						Scanner sc = new Scanner(new File("words.txt"));
						out.println(sc.nextLine());
					}
					catch(IOException e)
					{
						
					}
					finally
					{
						out.println("[");
					}
					break;
				}
			}
		} catch (IOException e) {

		}
	}
}