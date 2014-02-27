import java.util.ArrayList;


public class ParserTest {

  public static void main(String[] args) {

	int count = 0;
	for (String string : args) {
		if (!string.equals("\\")) {
			count++;
		}
	
	}
	String[] arr = new String[count];
	int count2 = 0;
	for (String string : args) {
		if (!string.equals("\\")) {
			arr[count2] = string;
			count2++;
		}
	
	}
	
	arr[0] = FormulaGenerator.generateFormula();
	
    for (String s : arr) {
    	//  s.replaceAll("\" \\", "\" ");
    final FormulaParser parser = new FormulaParser(s);   // Create a parser for String s.
    final Formula       p      = parser.parse();         // Parse all of s into formula p.
    final int           n      = parser.numberOfAtoms(); // After parsing, number of atoms is known.
    
    array.clear(); // clears the array from previous arguments
    ParserTest.createArg(new boolean[n], n); 
    
    boolean result = true;
    
    for(boolean[] a: array){
        boolean b = p.eval(a); //evaluates all possible results [true, false] [true, true] [false, false] [false, true] etc..
        if(b == false) {
            result = false;
        }
            
    }
      if(result == false)
          //prints out the result if it's false or true
          System.out.println("Formula " +p+ " isn't a tautology");
      else 
          System.out.println("Formula " +p+ " is a tautology");
      }
      
    
  }
    
    
    private static ArrayList<boolean[]> array = new ArrayList<boolean[]>(); //arraylist containing the boolean values from the arguments
      
      //a recursive method that creates the arguments
      public static void createArg(boolean[]a, int n) {
          if(n == 0) {
              array.add(a); //adds the boolean to the arraylist.
              return;
          }
          boolean[] a1 = a.clone();
           boolean[] a2 = a.clone(); //clones array and then adds true or false values
           a1[n-1] = true;
            a2[n-1] = false;
          
            createArg(a1, n - 1);
            createArg(a2, n - 1);
      }

 
  }

