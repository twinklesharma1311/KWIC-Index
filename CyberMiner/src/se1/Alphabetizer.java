package se1;

import java.util.ArrayList;
import java.util.Collections;


public class Alphabetizer {
	private String[][] list;
	private ArrayList<String> output;

	public Alphabetizer() {
		list = new String[0][0];
		output = new ArrayList<String>();
	}

	public ArrayList<String> inputList(String[][] l) {
		list = l;
		String temp;
		output = new ArrayList<String>();
		
		for (int i = 0; i < list.length; i++) {
			temp = "";
			for (String s : list[i]) {
				temp += s + " ";
			}
			output.add(temp);
		}
		Collections.sort(output);
		 
		System.out.println("\nAlphaetized sorted list:  ");
		for(String s : output)
			System.out.println(s);
		
		return output;
	}
}