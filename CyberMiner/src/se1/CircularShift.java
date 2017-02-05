package se1;

public class CircularShift {
	private String[] line;
	private String[][] output;

	public CircularShift() {
		line = new String[0];
		output = new String[0][0];
	}

	public String[][] inputLine(String s) {
		line = (s.toLowerCase()).split(" ");
		output = new String[line.length][line.length];
		
		System.out.println("\n The circular shifted lines are :");
		for (int i = 0; i < line.length; i++){
			for (int j = 0; j < line.length; j++) {
				output[i][j] = line[(i + j) % line.length];
				System.out.print(output[i][j] + " ");
			}
			System.out.println("\n");
		}
		return output;
	}
}