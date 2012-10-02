package us.nb9.tij.holding;
//: holding/ForEachCollections.java
// All collections work with foreach.
import java.util.*;

public class ForEachCollections {
  public static void main(String[] args) {
	  System.out.print("Enter main");
    Collection<String> cs = new LinkedList<String>();
    Collections.addAll(cs, "Take the long way home".split(" "));
    for(String s : cs) {
      System.out.print("'" + s + "' ");
    }
  }
} /* Output:
'Take' 'the' 'long' 'way' 'home'
*///:~