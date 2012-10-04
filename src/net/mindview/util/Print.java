//: net/mindview/util/Print.java
// Print methods that can be used without
// qualifiers, using Java SE5 static imports:
package net.mindview.util;

import java.io.*;

import android.util.Log;

public class Print {
	// Print with a newline:
	public static void print(Object obj) {
		System.out.println(obj);
	}

	// Print a newline by itself:
	public static void print() {
		System.out.println();
	}

	// Print with no line break:
	public static void printnb(Object obj) {
		System.out.print(obj);
	}

	// The new Java SE5 printf() (from C):
	public static PrintStream printf(String format, Object... args) {
		return System.out.printf(format, args);
	}

	public static void print_v(String tag, String msg) {
		Log.v(tag, msg);
	}

	public static void print_d(String tag, String msg) {
		Log.d(tag, msg);
	}

	public static void print_i(String tag, String msg) {
		Log.i(tag, msg);
	}

	public static void print_w(String tag, String msg) {
		Log.w(tag, msg);
	}

	public static void print_e(String tag, String msg) {
		Log.e(tag, msg);
	}
} // /:~
