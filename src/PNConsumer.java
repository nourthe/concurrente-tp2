import java.util.Queue;

class PNConsumer extends Thread {


	private Monitor mMonitor;
	private Recipe[] consumerRecipe = new Recipe[2];

	PNConsumer(Monitor m, String name, Queue<String> buffer1, Queue<String> buffer2) {
		this.mMonitor = m;
		this.setName(name);
		consumerRecipe[0] = new Recipe(buffer1, PN.Transitions.CONSUME_BUFFER_1,
				PN.Transitions.FINISHED_CONSUMING_BUFFER_1);
		consumerRecipe[1] = new Recipe(buffer2, PN.Transitions.CONSUME_BUFFER_2,
				PN.Transitions.FINISHED_CONSUMING_BUFFER_2);
	}

	@Override
	public void run() {
		while (!interrupted()) {
			int buffer = (Math.random() <= 0.5) ? 0 : 1;
			System.out.println(Thread.currentThread().getName() + " Quiero consumir de buffer " + (buffer+1));
			mMonitor.fireTransitions(consumerRecipe[buffer].getTransitions());
			String item = consumerRecipe[buffer].getBuffer().poll();
			System.out.println(Thread.currentThread().getName() + " Ya consumi " + item);
		}

		Thread.currentThread().interrupt();
	}
}
