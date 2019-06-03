import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("Hello World!");
        // new PNtest();
        Monitor monitor = new Monitor();

        // Producers
        List<Thread> producerThreadList = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            producerThreadList.add(new PNProducer(monitor));
            producerThreadList.get(i).start();
        }

        // Consumers
        List<Thread> consumerThreadList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            consumerThreadList.add(new PNConsumer(monitor));
            consumerThreadList.get(i).start();
        }

        producerThreadList.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        consumerThreadList.forEach(Thread::interrupt);
    }
}
