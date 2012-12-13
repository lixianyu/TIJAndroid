package us.nb9.tij.concurrency;
//: concurrency/CallableDemo.java
import java.util.concurrent.*;
import java.util.*;
import static net.mindview.util.Print.*;

class TaskWithResult implements Callable<String> {
	private int id;

	public TaskWithResult(int id) {
		this.id = id;
	}

	public String call() {
		return "result of TaskWithResult " + id;
	}
}

public class CallableDemo {
	private static String TAG = "CallableDemo";
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>();
		for (int i = 0; i < 10; i++)
			results.add(exec.submit(new TaskWithResult(i)));
		for (Future<String> fs : results)
			try {
				// get() blocks until completion:
				print_d(TAG, fs.get());
			} catch (InterruptedException e) {
				print_d(TAG, ""+e);
				return;
			} catch (ExecutionException e) {
				print_d(TAG, ""+e);
			} finally {
				exec.shutdown();
			}
	}
}
/* Output:
result of TaskWithResult 0
result of TaskWithResult 1
result of TaskWithResult 2
result of TaskWithResult 3
result of TaskWithResult 4
result of TaskWithResult 5
result of TaskWithResult 6
result of TaskWithResult 7
result of TaskWithResult 8
result of TaskWithResult 9
*///:~
