class PNProducer extends Thread {

	private Monitor mMonitor;

	public PNProducer(Monitor monitor) {
		mMonitor = monitor;
	}

	@Override
	public void run() {
		for (int i=0; i<100; i++) {
			System.out.println(Thread.currentThread().getName() +  " Quiero producir:" + i);
			mMonitor.produce("Produccion " + i);
			System.out.println(Thread.currentThread().getName() +  " Ya produje:" + i);
		}
	}
}
