class PNConsumer extends Thread {


	private Monitor mMonitor;

	PNConsumer(Monitor m, String name) {
		this.mMonitor = m;
		this.setName(name);
	}

	@Override
	public void run() {
		String item = "";
		while (!interrupted()) {
			System.out.println(Thread.currentThread().getName() +  " Quiero consumir");
			item = mMonitor.consume();
			if (!item.isEmpty())
			System.out.println(Thread.currentThread().getName() +  " Ya consumi " + item);
		}
	}
}
