import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Monitor {

	private final Queue<String> buffer1;
	private final Queue<String> buffer2;
	private final PN mPN;

	private final Lock mLock;

	private final Condition mNotFull;
	private final Condition mNotEmpty;

	Monitor() {
		buffer1 = new LinkedList<>();
		buffer2 = new LinkedList<>();

		mPN = new PN();

		mLock = new ReentrantLock(true);
		mNotFull = mLock.newCondition();
		mNotEmpty = mLock.newCondition();
	}

	int getBuffersLoad() {
		mLock.lock();
		try {
			return buffer1.size() + buffer2.size();
		}
		finally {
			mLock.unlock();
		}
	}
	void startProducing() {
		mLock.lock();
		try {
			while ( !mPN.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_1) &&
					!mPN.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_2)) {
				mNotFull.await();
			}
			if (mPN.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_1)) {
				mPN.fire(PN.Transitions.PRODUCE_BUFFER_1);
			}
			else if (mPN.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_2)) {
				mPN.fire(PN.Transitions.PRODUCE_BUFFER_2);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			mLock.unlock();
		}
	}
	void finishProducing(String data) {
		mLock.lock();
		if (mPN.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_1)) {
			buffer1.add(data);
			mPN.fire(PN.Transitions.FINISHED_PRODUCING_BUFFER_1);
		}
		else if (mPN.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_2)) {
			buffer2.add(data);
			mPN.fire(PN.Transitions.FINISHED_PRODUCING_BUFFER_2);
		}
		mNotEmpty.signal();
		mLock.unlock();
	}
	void produce(String data) {
		mLock.lock();
		try {
			while ( !mPN.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_1) &&
					!mPN.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_2)) {
				mNotFull.await();
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
			mNotEmpty.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			mLock.unlock();
		}

	}

	String consume() {
		mLock.lock();
		String item = "";
		try {
			while ( !mPN.isTransitionEnabled(PN.Transitions.CONSUME_BUFFER_1) &&
					!mPN.isTransitionEnabled(PN.Transitions.CONSUME_BUFFER_2)) {
				mNotEmpty.await();
			}
			if (mPN.isTransitionEnabled(PN.Transitions.CONSUME_BUFFER_1)) {
				mPN.fire(PN.Transitions.CONSUME_BUFFER_1);
				item = buffer1.poll();
				mPN.fire(PN.Transitions.FINISHED_CONSUMING_BUFFER_1);
			}
			else if (mPN.isTransitionEnabled(PN.Transitions.CONSUME_BUFFER_2)) {
				mPN.fire(PN.Transitions.CONSUME_BUFFER_2);
				item = buffer2.poll();
				mPN.fire(PN.Transitions.FINISHED_CONSUMING_BUFFER_2);
			}
			mNotFull.signal();
		} catch (InterruptedException e) {
			// Hay que llamar el metodo interrupt porque cuando se interrumpe la bandera Interrupted
			// se resetea y el run del consumer se va a ejecutar para siempre
			Thread.currentThread().interrupt();
		}
		finally {
			mLock.unlock();
		}

		return item;
	}

	public String[] getState() {
		mLock.lock();
	    String[] strings = {String.valueOf(buffer1.size()), String.valueOf(buffer2.size()), mPN.getMarkingString()};
		mLock.unlock();
	    return strings;
	}
}