package us.nb9.tij.strings;

import static net.mindview.util.Print.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
//: strings/BetterRead.java

public class BetterRead {
	private static final String TAG = "BetterRead";
	
	public static void main(String[] args) {
		Scanner stdin = new Scanner(SimpleRead.input);
		print_d(TAG, "What is your name?");
		String name = stdin.nextLine();
		print_d(TAG, name);
		print_d(TAG, "How old are you? What is your favorite double?");
		print_d(TAG, "(input: <age> <double>)");
		int age = stdin.nextInt();
		double favorite = stdin.nextDouble();
		print_d(TAG, ""+age);
		print_d(TAG, ""+favorite);
		print_v(TAG, String.format("Hi %s.\n", name));
		print_v(TAG, String.format("In 5 years you will be %d.\n", age + 5));
		print_v(TAG, String.format("My favorite double is %f.", favorite / 2));
		stdin.close();
	}
}
/* Output:
What is your name?
Sir Robin of Camelot
How old are you? What is your favorite double?
(input: <age> <double>)
22
1.61803
Hi Sir Robin of Camelot.
In 5 years you will be 27.
My favorite double is 0.809015.
*///:~
