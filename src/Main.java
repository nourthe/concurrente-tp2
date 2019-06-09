import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

public class Main {
    private static final int NUMBER_OF_PRODUCERS = 5;
    private static final int NUMBER_OF_CONSUMERS = 8;
    public static void main(String[] args) {

        // new PNtest();
        Monitor monitor = new Monitor();

        // Buffers
        Queue<String> buffer1 = new LinkedList<>();
        Queue<String> buffer2 = new LinkedList<>();

        // Producers
        List<Thread> producerThreadList = new ArrayList<>();

        IntStream.range(0,NUMBER_OF_PRODUCERS).forEachOrdered(i -> {
            producerThreadList.add(new PNProducer(monitor, "Producer-"+i, buffer1, buffer2,false));
            producerThreadList.get(i).start();
        });

        // Consumers
        List<Thread> consumerThreadList = new ArrayList<>();

        IntStream.range(0,NUMBER_OF_CONSUMERS).forEachOrdered(i -> {
            consumerThreadList.add(new PNConsumer(monitor, "Consumer-"+i, buffer1, buffer2));
            consumerThreadList.get(i).start();
        });

        // Loger create and start.
        //Loger loger = new Loger(monitor, producerThreadList, consumerThreadList, "out/log.txt");
        //loger.start();

        producerThreadList.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Producidos todos los items\n");

        while ((buffer1.size() & buffer2.size()) != 0){
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Consumidos todos los items\n");
        consumerThreadList.forEach(Thread::interrupt);
        //loger.interrupt();
    }
}
