import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Monitor {
	private final PN mPN;

	private final Lock mLock;
	private final HashMap<PN.Transitions, Condition> conditions = new HashMap<>();

	Monitor() {
		double[] initialMarking = {0,0,8,0,0,10,15,0,0,5};
		mPN = new PN(initialMarking);

		mLock = new ReentrantLock(true);
		// create one condition per transition
		for (PN.Transitions t : PN.Transitions.values()) conditions.put(t, mLock.newCondition());
	}

	public void fireTransitions(PN.Transitions... transitions) {
		mLock.lock();
		try {
			for (PN.Transitions t : transitions) {
				// sleep in queue until condition is met
				while (!mPN.isTransitionEnabled(t)) conditions.get(t).await();

				mPN.fire(t);
			}

			// send a signal to all conditions with enabled transitions
			for (PN.Transitions t: mPN.getEnabledTransitions()) conditions.get(t).signal();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			mLock.unlock();
		}
	}
}