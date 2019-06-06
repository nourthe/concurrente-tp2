import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Loger extends Thread {

    private Monitor mMonitor;
    private List<Thread> mProducerList;
    private List<Thread> mConsumerList;

    private FileWriter file;
    private PrintWriter pw;

    public Loger(Monitor mMonitor, List<Thread> mProducerList, List<Thread> mConsumerList, String fileLocation) {
        this.mMonitor = mMonitor;
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
        int each = 2; //seconds
        int i = 0;
        try{
            while(true){
                //Log
                pw.printf("________________________________\n");
                pw.printf("Time: %d seconds.\n", each*i);

                pw.printf("\n");

                //String[] state = mMonitor.getState();
                String[] state = { "1", "2" };

                pw.printf("Storage 1 load: %s\n", state[0]);
                pw.printf("Storage 2 load: %s\n", state[1]);

                pw.printf("\n");

                pw.printf("Petri Net Marking: %s\n", state[2]);

                for (Thread p: mProducerList) {
                    pw.printf(" Name: %s\tState: %s\n", p.getName(), p.getState());
                }
                for (Thread c: mConsumerList) {
                    pw.printf(" Name: %s\tState: %s\n", c.getName(), c.getState());
                }
                pw.printf("\n");
                //Sleep 2 secs
                Thread.sleep(each * 1000);
                i++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
