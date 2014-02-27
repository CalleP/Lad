import java.util.LinkedList;
import java.util.Random;

import javax.xml.stream.events.Characters;

public class FormulaGenerator {
	public static String generateFormula()
	{
		LinkedList<Character> allowedCharacters = new LinkedList<Character>();
		allowedCharacters.add('0');
		allowedCharacters.add('U');
		allowedCharacters.add('A');
		allowedCharacters.add('B');
		allowedCharacters.add('C');
		
		LinkedList<String> allowedConnectors = new LinkedList<String>();
		allowedConnectors.add("\\cap");
		allowedConnectors.add("\\cup");
		allowedConnectors.add("\\subseteq");
		allowedConnectors.add("=");
		allowedConnectors.add("\\");

		Random rand = new Random();
		
		int amountConnectors = rand.nextInt(11)+5;
		
		String output = "";
		for (int i = 0; i < amountConnectors; i++) {
			output = output + "(" +
					allowedCharacters.get(rand.nextInt(allowedCharacters.size())) + " " + 
					allowedConnectors.get(rand.nextInt(allowedConnectors.size())) + " ";
			if (i == amountConnectors - 1) {
				output += allowedCharacters.get(rand.nextInt(allowedCharacters.size()));
			}
		}
		for (int i = 0; i < amountConnectors; i++) {
			output += ")";
		}

		
		return output;
	}
	
	  public static void main(String[] args) {
		  System.out.println(generateFormula());
		  System.out.println("\2261");
	  }
}
