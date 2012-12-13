//: typeinfo/toys/ToyTest.java
// Testing class Class.
package us.nb9.tij.typeinfo.toys;

import static net.mindview.util.Print.*;

import java.util.HashSet;
import java.util.LinkedHashSet;

interface HasBatteries {
}

interface Waterproof {
}

interface Shoots {
}

class Toy {
	// Comment out the following default constructor
	// to see NoSuchMethodError from (*1*)
	Toy() {
	}

	Toy(int i) {
	}
}

class FancyToy extends Toy implements HasBatteries, Waterproof, Shoots {
	FancyToy() {
		super(1);
	}
}

public class ToyTest {
	static void printInfo(Class cc) {
		print("Enter printInfo(), cc = " + cc);
		print("Class name: " + cc.getName() + " is interface? ["
				+ cc.isInterface() + "]");
		print("Simple name: " + cc.getSimpleName());
		print("Canonical name : " + cc.getCanonicalName());
	}

	public static void main(String[] args) {
		Class c = null;
		try {
			c = Class.forName("us.nb9.tij.typeinfo.toys.FancyToy");
		} catch (ClassNotFoundException e) {
			print("Can't find FancyToy");
			System.exit(1);
		}
		printInfo(c);
		
		print(".................................");
		for (Class face : c.getInterfaces()) {
			printInfo(face);
		}
		print(".................................");
		
		Class up = c.getSuperclass();
		Object obj = null;
		try {
			// Requires default constructor:
			obj = up.newInstance();
		} catch (InstantiationException e) {
			print("Cannot instantiate");
			System.exit(1);
		} catch (IllegalAccessException e) {
			print("Cannot access");
			System.exit(1);
		}
		printInfo(obj.getClass());
	}
	
	private static void printClasses(Class cc) {
		print("Enter printClasses(), cc = " + cc);
		print("Class name: " + cc.getCanonicalName());
		Class ccSuper = null;
		while (true) {
			ccSuper = cc.getSuperclass();
			if (ccSuper == null) {
				break;
			}
			print("Class name: " + ccSuper.getCanonicalName());
			cc = ccSuper;
		}
		print("Leave printClasses()");
	}
	
	public static void test() {
		LinkedHashSet<Integer> hs = new LinkedHashSet<Integer>();
		printClasses(boolean.class);
	}
}
/* Output:
Class name: typeinfo.toys.FancyToy is interface? [false]
Simple name: FancyToy
Canonical name : typeinfo.toys.FancyToy
Class name: typeinfo.toys.HasBatteries is interface? [true]
Simple name: HasBatteries
Canonical name : typeinfo.toys.HasBatteries
Class name: typeinfo.toys.Waterproof is interface? [true]
Simple name: Waterproof
Canonical name : typeinfo.toys.Waterproof
Class name: typeinfo.toys.Shoots is interface? [true]
Simple name: Shoots
Canonical name : typeinfo.toys.Shoots
Class name: typeinfo.toys.Toy is interface? [false]
Simple name: Toy
Canonical name : typeinfo.toys.Toy
*///:~
