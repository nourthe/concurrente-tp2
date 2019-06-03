class PNConsumer extends Thread {


	private Monitor mMonitor;

	public PNConsumer(Monitor m) {
		this.mMonitor = m;
	}

	@Override
	public void run() {
		String item = "";
		while (!interrupted()) {
			System.out.println(Thread.currentThread().getName() +  " Quiero consumir");
			item = mMonitor.consume();
			System.out.println(Thread.currentThread().getName() +  " Ya consumi " + item);
		}
	}
}
