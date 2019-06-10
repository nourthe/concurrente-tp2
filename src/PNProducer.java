import java.util.Queue;

class PNProducer extends Thread {

	private Monitor mMonitor;
	private boolean produceOutside;
	private Recipe[] producerInsideRecipes = new Recipe[2];
	private Recipe[] producerOutsideRecipes = new Recipe[4];
	private String item;

	PNProducer(Monitor monitor, String name,Queue<String> buffer1,final Queue<String> buffer2, boolean produceOutside) {
		mMonitor = monitor;
		this.setName(name);
		this.produceOutside = produceOutside;
		this.producerInsideRecipes[0] = new Recipe();
		this.producerInsideRecipes[1] = new Recipe();

		this.producerOutsideRecipes[0] = new Recipe();
		this.producerOutsideRecipes[1] = new Recipe();
		this.producerOutsideRecipes[2] = new Recipe();
		this.producerOutsideRecipes[3] = new Recipe();

		this.producerInsideRecipes[0].addMapping(PN.Transitions.PRODUCE_BUFFER_1, () -> {});
		this.producerInsideRecipes[0].addMapping(PN.Transitions.FINISHED_PRODUCING_BUFFER_1,() -> buffer1.add(item));
		this.producerInsideRecipes[1].addMapping(PN.Transitions.PRODUCE_BUFFER_2, () -> {});
		this.producerInsideRecipes[1].addMapping(PN.Transitions.FINISHED_PRODUCING_BUFFER_2, () -> buffer2.add(item));


		this.producerOutsideRecipes[0].addMapping(PN.Transitions.PRODUCE_BUFFER_1, () -> {});
		this.producerOutsideRecipes[1].addMapping(PN.Transitions.PRODUCE_BUFFER_2, () -> {});
		this.producerOutsideRecipes[2].addMapping(PN.Transitions.FINISHED_PRODUCING_BUFFER_1,() -> buffer1.add(item));
		this.producerOutsideRecipes[3].addMapping(PN.Transitions.FINISHED_PRODUCING_BUFFER_2, () -> buffer2.add(item));
	}

	@Override
	public void run() {
		for (int i=0; i<10000; i++) {
			// Choose random buffer to produce to
			int buffer = (Math.random() <= 0.5) ? 0 : 1;
			item = "Item " + i;
			System.out.println(Thread.currentThread().getName() + ": Quiero producir: " + item + " en buffer " + (buffer+1));

			if (!produceOutside) {
				if (!mMonitor.fireTransitions(producerInsideRecipes[buffer].getTransitionMap())) {
					i--;
					continue;
				}
			} else {
				// TODO: make a dictionary inside Recipe to associate functions to transitions
				if (!mMonitor.fireTransitions(producerOutsideRecipes[buffer].getTransitionMap())) {
					i--; continue;
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!mMonitor.fireTransitions(producerOutsideRecipes[buffer+2].getTransitionMap())) {
					i--; continue;
				}
			}

			System.out.println(Thread.currentThread().getName() +  " Ya produje:" + i);
		}
	}
}
