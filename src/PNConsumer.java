import java.util.Queue;

class PNConsumer extends Thread {


	private Monitor mMonitor;
	private Recipe[] consumeInsideRecipe = new Recipe[2];
	private String item;

	PNConsumer(Monitor m, String name, Queue<String> buffer1, Queue<String> buffer2) {
		this.mMonitor = m;
		this.setName(name);
		this.consumeInsideRecipe[0] = new Recipe();
		this.consumeInsideRecipe[1] = new Recipe();

		this.consumeInsideRecipe[0].addMapping(PN.Transitions.CONSUME_BUFFER_1, () -> {});
		this.consumeInsideRecipe[0].addMapping(PN.Transitions.FINISHED_CONSUMING_BUFFER_1,() -> item = buffer1.poll());
		this.consumeInsideRecipe[1].addMapping(PN.Transitions.CONSUME_BUFFER_2, () -> {});
		this.consumeInsideRecipe[1].addMapping(PN.Transitions.FINISHED_CONSUMING_BUFFER_2, () -> item = buffer2.poll());
	}

	@Override
	public void run() {
		while (!interrupted()) {
			int buffer = (Math.random() <= 0.5) ? 0 : 1;
			System.out.println(Thread.currentThread().getName() + " Quiero consumir de buffer " + (buffer+1));
			if (!mMonitor.fireTransitions(consumeInsideRecipe[buffer].getTransitionMap())) continue;
			System.out.println(Thread.currentThread().getName() + " Ya consumi " + item);
		}
	}
}
