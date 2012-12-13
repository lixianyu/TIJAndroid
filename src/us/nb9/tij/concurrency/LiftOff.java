package us.nb9.tij.concurrency;
//: concurrency/LiftOff.java
// Demonstration of the Runnable interface.
import static net.mindview.util.Print.*;

public class LiftOff implements Runnable {
	private static final String TAG = "LiftOff";
	
	protected int countDown = 10; // Default
	private static int taskCount = 0;
	private final int id = taskCount++;

	public LiftOff() {
	}

	public LiftOff(int countDown) {
		this.countDown = countDown;
	}

	public String status() {
		return "#" + id + "(" + (countDown > 0 ? countDown : "Liftoff!")
				+ "), ";
	}

	public void run() {
		while (countDown-- > 0) {
			print_d(TAG, status());
			Thread.yield();
		}
	}
}
///:~
