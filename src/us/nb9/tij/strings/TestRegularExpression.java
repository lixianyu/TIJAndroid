package us.nb9.tij.strings;

//: strings/TestRegularExpression.java
// Allows you to easily try out regular expressions.
// {Args: abcabcabcdefabc "abc+" "(abc)+" "(abc){2,}" }
import java.util.regex.*;
import static net.mindview.util.Print.*;

public class TestRegularExpression {
	public static void test() {
//		main(new String[] {"abcabcabcdefabc", "abc+", "(abc)++", "(abc){2,}"});
//		main(new String[] {"Java now has regular expressions",
//			"^Java", "reg.*", "n.w\\s+h(a|i)s", "s?", "s*", "s+",
//			"s{4}", "s{1}", "s{0,3}"});
		main(new String[] {"Arline ate eight apples and one orange while Anita hadn't any",
				"(?i)((^[aeiou])|(\\s+[aeiou]))\\w+?[d]\\b"});
	}
	
	private static void main(String[] args) {
		if (args.length < 2) {
			print("Usage:\njava TestRegularExpression "
					+ "characterSequence regularExpression+");
			System.exit(0);
		}
		print("Input: \"" + args[0] + "\"");
		for (String curArg : args) {
			print("Regular expression: \"" + curArg + "\"");
			Pattern p = Pattern.compile(curArg);
			Matcher m = p.matcher(args[0]);
			while (m.find()) {
				print("Match \"" + m.group() + "\" at positions " + m.start()
						+ "-" + (m.end() - 1));
			}
		}
	}
} /*
 * Output: Input: "abcabcabcdefabc" Regular expression: "abcabcabcdefabc" Match
 * "abcabcabcdefabc" at positions 0-14 Regular expression: "abc+" Match "abc" at
 * positions 0-2 Match "abc" at positions 3-5 Match "abc" at positions 6-8 Match
 * "abc" at positions 12-14 Regular expression: "(abc)+" Match "abcabcabc" at
 * positions 0-8 Match "abc" at positions 12-14 Regular expression: "(abc){2,}"
 * Match "abcabcabc" at positions 0-8
 */// :~
