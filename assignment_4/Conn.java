
/** Binary connective. */
public enum Conn {
  INTER("∩"), UNION("∪"), DIFF("\\"),
  SUBSETEQ("⊆"), EQUAL("=");
  
  /** How to print the connective. */
  public final String name;
  
  Conn (String name) { this.name = name; }
  
  public String toString() { return name; }
}