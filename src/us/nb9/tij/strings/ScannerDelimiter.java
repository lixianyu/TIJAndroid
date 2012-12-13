package us.nb9.tij.strings;

//: strings/ScannerDelimiter.java

import static net.mindview.util.Print.*;

import java.util.Scanner;

public class ScannerDelimiter {
	private static final String TAG = "ScannerDelimiter";
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner("12, 42, 78, 99, 42");
//		scanner.useDelimiter("\\s*,\\s*");
		while (scanner.hasNextInt()) {
			print_v(TAG, ""+scanner.nextInt());
		}
	}
}
/* Output:
12
42
78
99
42
*///:~
