import java.util.Queue;

class PNProducer extends Thread {

	private Monitor mMonitor;
	private boolean produceOutside;
	private Recipe[] producerRecipes = new Recipe[2];

	PNProducer(Monitor monitor, String name, Queue<String> buffer1, Queue<String> buffer2, boolean produceOutside) {
		mMonitor = monitor;
		this.setName(name);
		this.produceOutside = produceOutside;
		// recipe to produce to buffer 1
		this.producerRecipes[0] = new Recipe(buffer1,
				PN.Transitions.PRODUCE_BUFFER_1, PN.Transitions.FINISHED_PRODUCING_BUFFER_1);
		// recipe to produce to buffer 2
		this.producerRecipes[1] = new Recipe(buffer2,
				PN.Transitions.PRODUCE_BUFFER_2, PN.Transitions.FINISHED_PRODUCING_BUFFER_2);
	}

	@Override
	public void run() {
		for (int i=0; i<10000; i++) {
			// Choose random buffer to produce to
			int buffer = (Math.random() <= 0.5) ? 0 : 1;
			String item = "Item " + i;
			System.out.println(Thread.currentThread().getName() + ": Quiero producir: " + item + " en buffer " + (buffer+1));

			if (!produceOutside) {
				mMonitor.fireTransitions(producerRecipes[buffer].getTransitions());
			} else {
				// TODO: make a dictionary inside Recipe to associate functions to transitions
				mMonitor.fireTransitions(producerRecipes[buffer].getTransitions()[0]);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mMonitor.fireTransitions(producerRecipes[buffer].getTransitions()[1]);
			}

			producerRecipes[buffer].getBuffer().add(item);
			System.out.println(Thread.currentThread().getName() +  " Ya produje:" + i);
		}
	}
}
