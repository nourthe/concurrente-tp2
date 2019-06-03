class PNProducer extends Thread {

	private Monitor mMonitor;

	PNProducer(Monitor monitor, String name) {
		mMonitor = monitor;
		this.setName(name);
	}

	@Override
	public void run() {
		for (int i=0; i<10000; i++) {
			System.out.println(Thread.currentThread().getName() +  " Quiero producir:" + i);
			mMonitor.produce("Produccion " + i);
			System.out.println(Thread.currentThread().getName() +  " Ya produje:" + i);
		}
	}
}
