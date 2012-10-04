package us.nb9.tij.strings;

//: strings/Finding.java
import java.util.regex.*;
import static net.mindview.util.Print.*;

public class Finding {
	public static void main(String[] args) {
		print_v(Finding.class.getSimpleName(),"Enter main");
		Matcher m = Pattern.compile("\\w+").matcher(
				"Evening is full of the linnet's wings");
		while (m.find()) {
			print_d(Finding.class.getSimpleName(), m.group() + " ");
		}
		print();
		print_i(Finding.class.getSimpleName(),"......................................................................");
		int i = 0;
		while (m.find(i)) {
			print_d(Finding.class.getSimpleName(), m.group() + " ");
			i++;
			print_i(Finding.class.getSimpleName(),"......................................................................");
		}
		print_v(Finding.class.getSimpleName(), "Leave main");
	}
} /*
 * Output: Evening is full of the linnet s wings Evening vening ening ning ing
 * ng g is is s full full ull ll l of of f the the he e linnet linnet innet nnet
 * net et t s s wings wings ings ngs gs s
 */// :~
