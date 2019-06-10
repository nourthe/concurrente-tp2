import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

public class Loger extends Thread {

    private final PN mPN;
    private List<Collection> mBufferList;
    private List<Thread> mProducerList;
    private List<Thread> mConsumerList;

    private FileWriter file;
    private PrintWriter pw;

    public Loger(PN pn, List<Collection> mBufferList, List<Thread> mProducerList, List<Thread> mConsumerList, String fileLocation) {
        this.mPN = pn;
        this.mBufferList = mBufferList;
        this.mProducerList = mProducerList;
        this.mConsumerList = mConsumerList;

        try {
            file = new FileWriter(fileLocation);
            pw = new PrintWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int each = 2; //how many seconds between iterations
        int i = 0;
        try{
            while(true){
                //Log
                pw.printf("________________________________\n");
                pw.printf("Time: %d seconds.\n", each*i);

                pw.printf("\n");

                pw.printf("Petri Net Marking: %s\n", mPN.getMarkingString());

                pw.printf("\n");

                pw.printf("Buffers Loads:\n");
                int b = 0;
                for (Collection buffer: mBufferList) {
                    b++;
                    pw.printf(" Buffer: %s\tSize: %s\n", b, buffer.size());
                }
                pw.printf("Threads States:\n");
                for (Thread p: mProducerList) {
                    pw.printf(" Name: %s\tState: %s\n", p.getName(), p.getState());
                }
                for (Thread c: mConsumerList) {
                    pw.printf(" Name: %s\tState: %s\n", c.getName(), c.getState());
                }

                pw.printf("\n");

                //Sleep 'each' seconds
                Thread.sleep(each * 1000);
                i++;
            }
        } catch (InterruptedException e) {
            System.out.println("Loger detenido.");
        } finally {
            //Closing file
            try {
                file.close();
                pw.close();
            }
            catch (IOException e) { e.printStackTrace(); }
        }
    }
}
