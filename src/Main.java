import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
    private static final int NUMBER_OF_PRODUCERS = 5;
    private static final int NUMBER_OF_CONSUMERS = 8;
    public static void main(String[] args) {

        System.out.println("Hello World!");
        // new PNtest();
        Monitor monitor = new Monitor();

        // Producers
        List<Thread> producerThreadList = new ArrayList<>();

        IntStream.range(0,NUMBER_OF_PRODUCERS).forEachOrdered(i -> {
            producerThreadList.add(new PNProducer(monitor, "Producer-"+i));
            producerThreadList.get(i).start();
        });

        // Consumers
        List<Thread> consumerThreadList = new ArrayList<>();

        IntStream.range(0,NUMBER_OF_CONSUMERS).forEachOrdered(i -> {
            consumerThreadList.add(new PNConsumer(monitor, "Consumer-"+i));
            consumerThreadList.get(i).start();
        });

        producerThreadList.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Producidos todos los items\n");
        System.out.println(monitor.getBuffersLoad());

        while (monitor.getBuffersLoad() != 0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Consumidos todos los items\n");
        consumerThreadList.forEach(Thread::interrupt);
    }
}
