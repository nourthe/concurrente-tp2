import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Monitor {
	private final PN mPN;

	private final Lock mLock;
	private final LinkedList<Condition> conditions = new LinkedList<>();

	Monitor() {
		double[] initialMarking = {0,0,8,0,0,10,15,0,0,5};
		mPN = new PN(initialMarking);

		mLock = new ReentrantLock(true);
		// create one condition per transition
		for (int i = 0; i < mPN.getTotalTransitions(); i++) {
			conditions.add(mLock.newCondition());
		}
	}

	public void fireTransitions(PN.Transitions... transitions) {
		mLock.lock();
		try {
			for (PN.Transitions t : transitions) {
				// sleep in queue until condition is met
				while (!mPN.isTransitionEnabled(t)) conditions.get(t.getTransitionCode()).await();

				mPN.fire(t);
			}

			// send a signal to all conditions with enabled transitions
			for (PN.Transitions t: mPN.getEnabledTransitions()) conditions.get(t.getTransitionCode()).signal();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			mLock.unlock();
		}
	}
}