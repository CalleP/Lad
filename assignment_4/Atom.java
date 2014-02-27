/** Set variables (atoms). */
public class Atom extends Formula {

  /** Valid atom identifiers are >= MIN. */  
  public static final int MIN      = -2;

  /** A special atom denoting the empty set. */
  public static final int EMPTYSET = -2;

  /** A special atom denoting the universe. */
  public static final int UNIVERSE = -1;

  /** The unique identifier id >= MIN of the atom. 
      Normal atoms (set variables) have id >= 0.   */
  public final int id;
  
  /** The name of the identifier used for printing (optional). */
  public final String name;
 
  /** Construct a set variable.
   * @param id >= MIN.
   * @param name maybe be empty, but not null.  */
  Atom (int id, String name) {
    if (id < MIN || name == null) throw new IllegalArgumentException();
    this.id   = id;
    this.name = name;
  }

  /** Construct a set variable with default name.
   * @param id >= MIN.  */
  Atom (int id) { this (id, ""); }

  /** Print atom. */  
  public String toString() {
    switch (id) {
    case EMPTYSET: return "∅";
    case UNIVERSE: return "U";
    default      : if (name.isEmpty()) return "A" + id;
                   else return name;
    }
  }
  
  public boolean eval (boolean[] valuation) {

	  if (id == -1) return true;
	  if (id == -2) return false;
	  return valuation[id];
	  
	 
    // TODO

  }
}
