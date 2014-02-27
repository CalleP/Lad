
/** Binary connection of two sub formulas. */
public class Bin extends Formula {
  
  /** Left sub formula. */
  public final Formula left;
  
  /** The connective. */
  public final Conn connective;
  
  /** Right sub formula. */
  public final Formula right;
  
  Bin (Formula left, Conn connective, Formula right) {
    this.left       = left;
    this.connective = connective;
    this.right      = right;
  }
  
  public String toString() { 
    return "(" + left.toString() + 
           " " + connective.toString() + 
           " " + right.toString() + ")";
  }
  
  public boolean implication(boolean l, boolean r)
  {
	  if (l == r) return true;
	  if (l == true && r == false) return true;
	  return false;

  }
  

  public boolean eval (boolean[] valuation) {
	  boolean l = left.eval(valuation);
	  boolean r = right.eval(valuation);
	  	  
	  
	  switch (connective) {
      case DIFF:   return l != r;
      case EQUAL:  return l == r; 
      case INTER:  return l && r;
      case SUBSETEQ: return implication(r,r);
      case UNION:	return l || r;
      default      : throw new UnsupportedOperationException();

   
	  }
	  
	 

	  





  }
}
