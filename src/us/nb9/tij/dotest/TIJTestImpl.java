package us.nb9.tij.dotest;

import static net.mindview.util.Print.*;
import us.nb9.tij.TIJAndroidConfig;
import us.nb9.tij.strings.*;

public class TIJTestImpl implements TIJTest {
	private static String TAG = "TIJTestImpl";
	private static final boolean DEBUG = TIJAndroidConfig.DEBUG && true;
	
	@Override
	public void doTest() {
		print_v(TAG, "Enter doTest()");
//		 ArrayListDisplay.main(null);
//		 AddingGroups.main(null);
//		 AsListInference.main(null);
//		 PrintingContainers.main(null);
//		 QueueDemo.main(null);
//		 PriorityQueueDemo.main(null);
//		 CollectionSequence.main(null);
//		 NonCollectionSequence.main(null);
//		 LiteralPetCreator.main(null);
//		 ForEachCollections.main(null);
//		 Rudolph.main(null);
//		 TestRegularExpression.test();
//		 Groups.test();
//		SplitDemo.main(null);
		TheReplacements.test();
		print_v(TAG, "Leave doTest()");
	}

}
