import java.util.TreeMap;

/** Classification of tokens for parsing formulas. */
enum TokenType {
  
  /** Set variable: U is for universe. */
  ATOM,

  /** Empty set: "0" or "∅". */
  EMPTYSET,
  
  /** Opening parenthesis: "(". */
  LPAREN,
  
  /** Closing parenthesis: ")". */
  RPAREN,

  /** Intersection symbol: "∩" or "\cap". */
  INTER, 

  /** Union symbol: "∪" or "\cup". */
  UNION,

  /** Difference symbol: "\" or "-". */
  DIFF,
  
  /** Implication symbol: "⊆" or "\subseteq". */
  SUBSETEQ,
  
  /** Equivalence symbol: "=" or "≡". */
  EQUAL,
  
  /** Invalid character. */
  INVALID, 
  
  /** End of file (or input). */
  EOF;
}

/** A single token of input. */
class Token {
  
  /** Type of token.  That info suffices for most tokens. */
  final public TokenType type;
  
  /** Value of token.  Interesting for ATOM. */
  final public String value;

  Token(TokenType type, String value) {
    this.type = type;
    this.value = value;
  }
  
  public String toString() { return value; }

  /** Returns the value of the token as connective.  null if it is no connective. */
  public Conn connective() {
    switch (type) {
    case INTER   : return Conn.INTER;
    case UNION   : return Conn.UNION;
    case DIFF    : return Conn.DIFF;
    case SUBSETEQ: return Conn.SUBSETEQ;
    case EQUAL   : return Conn.EQUAL;
    default      : return null;
    }
  }

  static Token LPAREN   = new Token(TokenType.LPAREN,   "(");
  static Token RPAREN   = new Token(TokenType.RPAREN,   ")");
  static Token EMPTYSET = new Token(TokenType.EMPTYSET, "∅");
  static Token INTER    = new Token(TokenType.INTER,    "∩");
  static Token UNION    = new Token(TokenType.UNION,    "∪");
  static Token DIFF     = new Token(TokenType.DIFF,     "-");
  static Token SUBSETEQ = new Token(TokenType.SUBSETEQ, "⊆");
  static Token EQUAL    = new Token(TokenType.EQUAL,    "≡");
  static Token INVALID  = new Token(TokenType.INVALID,  "" );
  static Token EOF      = new Token(TokenType.EOF,      "" );
}

/** Transform an input string into a sequence of tokens. 
 * 
 *  Create new Scanner(s) from input string s and 
 *  then iterate .next() to get all the tokens in sequence.
 *  Use .peek() to look ahead at next token. 
 *  Use .next() also to remove tokens you peeked at.  
 * 
 */
class Scanner {
  
  /** The input to be transformed into tokens.  
   *  Invariant: never starts with whitespace. */
  private StringBuilder input;
  
  /** The next token; null if we do not know it yet. */
  private Token token = null;
  
  /** Length of next token (number of characters). */
  private int tokenLength = 0;

  /** Construct a scanner for String s. 
   * 
   * @param s  Input for parsing.
   */
  Scanner (String s) {
    input = new StringBuilder(s);
    trim(0);  // To ensure invariant.
  }
  
  /** Discard i characters plus following whitespace. */
  private void trim(int i) {
    final int n = input.length();
    while (i < n && Character.isWhitespace(input.charAt(i))) 
      i++;
    input.delete(0, i);
  }

  /** Match beginning of input against string s.
   * 
   * @param s
   * @return true if current input starts with s. 
   *              In this case, tokenLength is set to the length of s.
   */
  private boolean startsWith(String s) {
    final int n = input.length();
    final int i = s.length();
    if (i <= n && input.substring(0, i).equals(s)) {
      tokenLength = i;
      return true;
    } else
      return false;
  }

  /** If input starts with alphanumeric characters (atom), return the atom and
   *  set tokenLength appropriately. Otherwise, return null.
   */
  private String startsWithAtom() {
    final int n = input.length();
    int i = 0;
    while (i < n && Character.isLetterOrDigit(input.charAt(i)))
      i++;
    if (i > 0) {
      final String atom = input.substring(0, i);
      tokenLength = i;
      return atom;
    } else
      return null;
  }

  /** Determine next token in input, but do not remove it from input.
   *  @return Next token (never null) in input.
   */
  private Token scanToken() {
    if (input.length() == 0)      return Token.EOF;
    if (startsWith("\\cap"))      return Token.INTER;
    if (startsWith("\\cup"))      return Token.UNION;
    if (startsWith("\\subseteq")) return Token.SUBSETEQ;
    if (startsWith("("))          return Token.LPAREN;
    if (startsWith(")"))          return Token.RPAREN;
    if (startsWith("∩"))          return Token.INTER;
    if (startsWith("∪"))          return Token.UNION;
    if (startsWith("-"))          return Token.DIFF;
    if (startsWith("\\"))         return Token.DIFF;
    if (startsWith("⊆"))          return Token.SUBSETEQ;
    if (startsWith("="))          return Token.EQUAL;
    if (startsWith("≡"))          return Token.EQUAL;
    if (startsWith("∅"))          return Token.EMPTYSET;
    final String atom = startsWithAtom();
    if (atom != null)             return new Token(TokenType.ATOM, atom);
    return Token.INVALID;
  }
 
  /** Look ahead at next token.  Do not modify input.
   * 
   * @return Next token to come (never null).
   */
  public Token peek() {
    if (token == null) token = scanToken();
    return token;
  }
  
  /** Remove token peeked at from input. */
  private void advance() {
    trim(tokenLength);
    tokenLength = 0;
    token = null;
  }
  
  /** Main method.  Get the next token and remove it from input.
   * 
   * @return Next token (never null) in input.
   */
  public Token next() {
    final Token t = peek();
    advance();
    return t;
  }
  
  /** Get the remaining input as string.
   * 
   * @return The remaining input (includes token peeked at).
   */
  public String remainingInput() { return input.toString(); }
}

/** Exception during parsing. Thrown if input does not match grammar.
 * 
 * @author Andreas Abel
 * @since 2014 February 19
 */
class ParseError extends RuntimeException {
  String msg;

  ParseError(String msg) { this.msg = msg; }

  public String toString() { return msg; }
}

/** Dictionary translating atom names into unique identifiers. */
class AtomDict {
  
  /** Bijective map from atom names to atom identifiers. */
  private TreeMap<String, Integer> atomDict = new TreeMap();
  
  /** Next available unique atom identifier. Non-negative. */
  private int nextAtomId = 0;

  /** The atom dictionary is pre-initialized with special atoms
      for the empty set and the universe. */
  public AtomDict() {
    atomDict.put("0", Atom.EMPTYSET);
    atomDict.put("∅", Atom.EMPTYSET);
    atomDict.put("U", Atom.UNIVERSE);
  }

  /** Get the number of different atoms seen so far. */
  public int numberOfAtoms() { return nextAtomId; }
  
  /** Get the unique identifier of atom as stored in atomDict. 
   *  If atom is new, create fresh identifier and add it to atomDict.
   * 
   * @param atom Name of atom.
   * @return Unique identifier of atom.
   */
  public int atomId(String atom) {
    final Integer val = atomDict.get(atom);
    if (val == null) {
      final int id = nextAtomId++;
      atomDict.put(atom, id);
      return id;
    } else return val;
  }
  
  /** Create an atomic propositional formula from its name.
   * 
   * @param s Name of the atom.
   * @return Atom with correct unique identifier.
   */
  public Atom atom(String s) {
    return new Atom(atomId(s), s);
  }
}

/**
 * A simple parser for fully parenthesized formulas of propositional logic.
 * 
 * This is a recursive descent parser for the grammar:
 * 
 *   P ::= alphanum | P * P | (P)
 * 
 * where * stands for a binary connective. This parser does not implement
 * operator precedences, thus formulas such as
 * 
 *   P ∩ Q ∩ R
 * 
 * need to be parenthesized to either (P ∩ Q) ∩ R or P ∩ (Q ∩ R).
 * 
 * The parser has the following states:
 * 
 * 0. parse (initial state): parseFormula and then expect end of input;
 * 1. parseFormula: parseFactor and then parseConnective;
 * 2. parseFactor: parse either a) parenthesized formula, negated factor, or atom;
 * 3. parseConnective: if we are at a connective, parseFactor, end formula.
 * 
 * @author Andreas Abel
 * @since 2014 February 19
 */
public class FormulaParser {

  private Scanner  scanner;
  private AtomDict atomDict;

  FormulaParser(String s) {
    scanner  = new Scanner(s);
    atomDict = new AtomDict();
  }

  private <A> A parseError(String error) {
    throw new ParseError("Parse error: " + error + " at " + scanner.remainingInput());
  }
  
  /** Main method.  Parses the whole input into a single formula. 
   * 
   * @return Formula (never null).
   */
  public Formula parse() {
    final Formula p = parseFormula();
    parseEOF();
    return p;
  }
  /** Get the number of different atoms seen so far. */
  public int numberOfAtoms() { return atomDict.numberOfAtoms(); }
  
  /** Parse a formula.
   * 
   * @return Formula (never null).
   */
  private Formula parseFormula() {
    final Formula p = parseFactor();
    return parseConnective(p);
  }

  /** Parse a factor of a formula, i.e., an atom, a negated factor, or a parenthesized formula. 
   * @return Formula (never null).
   */
  private Formula parseFactor() {
    final Token t = scanner.peek();
    switch (t.type) {
    case LPAREN:
      scanner.next(); 
      final Formula p = parseFormula();
      parseRParen();
      return p;
    case EMPTYSET:
      scanner.next(); 
      return atomDict.atom("∅");
    case ATOM:
      scanner.next(); 
      return atomDict.atom(t.value);
    default: 
      return parseError("beginning of formula expected");
    }
  }
  
  /** Check if next token is a connective, if yes, parse the second factor.
   *  If no, just return the formula p we have so far.
   *  
   *  @param p Formula we have parsed so far.
   *  @return p or completion of p into a binary connective.
   */
  private Formula parseConnective(Formula p) {
    final Token t = scanner.peek();
    final Conn conn = t.connective();
    if (conn == null) return p;
    else {
      scanner.next();
      return new Bin(p, conn, parseFactor());
    }
  }
  
  /** Ensure we have a closing parenthesis next and skip over it. */
  private void parseRParen() {
    if (scanner.peek() != Token.RPAREN) parseError("')' expected") ;
    scanner.next();  // Discard RPAREN.
  }

  /** Ensure we have reached the end of input. */
  private void parseEOF() {
    if (scanner.peek() != Token.EOF) 
    	parseError("end of input expected");
  }

  /** Test the parser. */
  public static void main(String[] args) {
    // Test the scanner.
    final Scanner scanner = new Scanner("(A ∪ B) ⊆ (!A∩B)");
    Token t = scanner.next(); 
    while (t != Token.EOF) { 
      System.out.print(t.toString());
      t = scanner.next();
    }
    System.out.println();
    // Test the parser.
    System.out.println(new FormulaParser("(A∪B)=!(!A-B)").parse().toString());
    System.out.println(new FormulaParser("(A∩B)=!(!A∪!B)").parse().toString());
    try {
      System.out.println(new FormulaParser("(A&B<->!(A->!B)").parse().toString());
      throw new RuntimeException("Failing test case passed.");
    } catch (ParseError e) {}  
  }

}