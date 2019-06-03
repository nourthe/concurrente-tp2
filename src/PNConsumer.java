class PNConsumer extends Thread {


	private Monitor mMonitor;

	public PNConsumer(Monitor m) {
		this.mMonitor = m;
	}

	@Override
	public void run() {
		while (true) {
			System.out.println(Thread.currentThread().getName() +  " Quiero consumir");
			mMonitor.consume();
			System.out.println(Thread.currentThread().getName() +  " Ya consumi");

		}
	}
}
