package Assignment2;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Queue implements Runnable{
    BlockingQueue<Client> clients;
    public int waitingPeriod=0;
    private int id;
    private int stop;
    public Queue(int id,int capacity){
        this.id=id;
        clients=new ArrayBlockingQueue<Client> (capacity);
        clients.clear ();
        stop=0;
    }
    public void addClient(Client client){
        synchronized (this) {
            clients.add ( client );
            waitingPeriod = waitingPeriod + client.service;//update the queue waiting time
                notify ();//notify the queue
        }
    }
    public String printState() {
        String s="Queue "+id+":";
        if(clients.isEmpty ())
            s=s+"closed";
        else{
            for (Client client:clients) {
                s=s+client.printClient ();
            }
        }
        return s;
        }
    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                while (!clients.isEmpty ()) {
                    clients.peek ().service++;
                    while (true) {
                       if( clients.peek ().service == 0){
                           //if the client is served we remove him from the queue
                           clients.peek ().timeSpent--;
                           clients.remove ( clients.peek () );
                           if(clients.isEmpty ()){
                               //if the queue is empty the waiting time will be 0
                               waitingPeriod=0;
                           }
                           break;
                       }
                        try {
                            Thread.sleep ( 10 );
                        } catch (InterruptedException e) {
                            e.printStackTrace ();
                        }
                        //update the clients serving time and time spend in the queue
                        clients.peek ().service--;
                        clients.peek ().timeSpent++;
                        waitingPeriod--;
                    }
                }
                //to kill the queue when needed
                if(stop==1)
                    break;
                try {
                    this.wait ();
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            }
        }
    }
    public String getClients(){
        String s="Queue "+id+" clients:";
        for (Client client:clients) {
            s=s+ client.printClient ();
        }
        return s;
    }
    void modifyStop(int stop){
        synchronized (this) {
            //stop the queue if there are no more clients to be added
            this.stop = stop;
            notify ();
        }
    }
}
