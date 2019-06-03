import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Monitor {

	private final Queue<String> buffer1 = new LinkedList<>();
	private final Queue<String> buffer2 = new LinkedList<>();
	private final PN mPN = new PN();

	private final Lock mLock = new ReentrantLock();

	private final Condition mBothNotFull = mLock.newCondition();
	private final Condition mBothNotEmpty = mLock.newCondition();

	public Monitor() {
	}

	public void produce(String data) {
		mLock.lock();
		try {
			List<PN.Transitions> availableTransitions = mPN.getEnabledTransitions();
			while ( !availableTransitions.contains(PN.Transitions.PRODUCE_BUFFER_1) &&
					!availableTransitions.contains(PN.Transitions.PRODUCE_BUFFER_2)) {
				mBothNotFull.await();
			}
			if (mPN.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_1)) {
				mPN.fire(PN.Transitions.PRODUCE_BUFFER_1);
				buffer1.add(data);
				mPN.fire(PN.Transitions.FINISHED_PRODUCING_BUFFER_1);
			}
			else if (mPN.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_2)) {
				mPN.fire(PN.Transitions.PRODUCE_BUFFER_2);
				buffer2.add(data);
				mPN.fire(PN.Transitions.FINISHED_PRODUCING_BUFFER_2);
			}
			mBothNotEmpty.signal();
		} catch (InterruptedException e) {

		}
		finally {
			mLock.unlock();
		}

	}

	public void consume() {
		mLock.lock();
		try {
			List<PN.Transitions> availableTransitions = mPN.getEnabledTransitions();
			while ( !availableTransitions.contains(PN.Transitions.CONSUME_BUFFER_1) &&
					!availableTransitions.contains(PN.Transitions.CONSUME_BUFFER_2)) {
				mBothNotEmpty.await();
			}
			if (mPN.isTransitionEnabled(PN.Transitions.CONSUME_BUFFER_1)) {
				mPN.fire(PN.Transitions.CONSUME_BUFFER_1);
				buffer1.poll();
				mPN.fire(PN.Transitions.FINISHED_CONSUMING_BUFFER_1);
			}
			else if (mPN.isTransitionEnabled(PN.Transitions.CONSUME_BUFFER_2)) {
				mPN.fire(PN.Transitions.CONSUME_BUFFER_2);
				buffer2.poll();
				mPN.fire(PN.Transitions.FINISHED_CONSUMING_BUFFER_2);
			}
			mBothNotFull.signal();
		} catch (InterruptedException e) {

		}
		finally {
			mLock.unlock();
		}
	}
}