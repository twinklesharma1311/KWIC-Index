package se1;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Filter {
	private ArrayList<String> input;

	public ArrayList<String> inputList(ArrayList<String> l) {
		input = l;
		
		for (int i = input.size() - 1; i >= 0; i--) {
			String token = (input.get(i)).substring(0,(input.get(i)).indexOf(" "));
			token = token.toLowerCase();
			System.out.println("\nIn filter class, token: " + token);
			
			Pattern p = Pattern.compile("[a-zA-Z0-9]*");
			Matcher m = p.matcher(token);
			
			if (!m.matches()) {
				input.remove(i);
				System.out.println("Removing the token, pattern does not match");
			} else if (token.equals("of") || token.equals("and")
					|| token.equals("not") || token.equals("is")
					|| token.equals("with") || token.equals("have")
					|| token.equals("but") || token.equals("so") || token.equals("for") || token.equals("the")) {
				input.remove(i);
				System.out.println("Token is a noise word, removing it");
			}
		}
		return input;
	}
}