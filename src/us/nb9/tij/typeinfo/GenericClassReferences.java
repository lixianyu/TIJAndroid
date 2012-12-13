package us.nb9.tij.typeinfo;
//: typeinfo/GenericClassReferences.java

public class GenericClassReferences {
	public static void main(String[] args) {
		Class intClass = int.class;
		Class<Double> genericIntClass = double.class;
		genericIntClass = Double.class; // Same thing
		intClass = double.class;
		genericIntClass = double.class; // Illegal
	}
}
///:~
