package us.nb9.tij.strings;

//: strings/Groups.java
import java.util.HashSet;
import java.util.Set;
import java.util.regex.*;
import static net.mindview.util.Print.*;

public class Groups {
	static public final String POEM = "Twas brillig, and the slithy toves\n"
			+ "Did gyre and gimble in the wabe.\n"
			+ "All mimsy were the borogoves,\n"
			+ "And the mome raths outgrabe.\n\n"
			+ "Beware the Jabberwock, my son,\n"
			+ "The jaws that bite, the claws that catch.\n"
			+ "Beware the Jubjub bird, and shun\n"
			+ "The frumious Bandersnatch.";

	public static void main(String[] args) {
		Matcher m = Pattern.compile("(?m)(\\S+)\\s+((\\S+)\\s+(\\S+))$")
				.matcher(POEM);
		while (m.find()) {
//			for (int j = 0; j <= m.groupCount(); j++) {
//				printnb("[" + m.group(j) + "]");
//			}
			print_v("Groups", "m.groupCount() = " + m.groupCount());
			printnb(m.group() + " ");
			print();
		}
	}
	
	/**
	 * 找出所有不以大写字母开头的词，不重复地计算其个数。
	 */
	public static void test() {
		print_d("Groups", "Enter test()");
		Matcher m = Pattern.compile("\\b[a-z]+\\b").matcher(POEM);
		int count = 0;
		Set<String> sSet = new HashSet<String>();
		String curGroup = "";
		while (m.find()) {
			curGroup = m.group();
			curGroup += "......";
			printnb(curGroup + " ");
			if (sSet.add(curGroup)) {
				count++;
			}
			print();
		}
		print_d("Groups", "Leave test(), count = " + count);
	}
} /*
 * Output: [the slithy toves][the][slithy toves][slithy][toves] [in the
 * wabe.][in][the wabe.][the][wabe.] [were the borogoves,][were][the
 * borogoves,][the][borogoves,] [mome raths outgrabe.][mome][raths
 * outgrabe.][raths][outgrabe.] [Jabberwock, my son,][Jabberwock,][my
 * son,][my][son,] [claws that catch.][claws][that catch.][that][catch.] [bird,
 * and shun][bird,][and shun][and][shun] [The frumious
 * Bandersnatch.][The][frumious Bandersnatch.][frumious][Bandersnatch.]
 */// :~
