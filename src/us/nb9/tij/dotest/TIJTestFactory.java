package us.nb9.tij.dotest;

public class TIJTestFactory {
	private TIJTestFactory() {}
	
	public static TIJTest createTest() {
		return new TIJTestImpl();
	}
}
