package Assignment2;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private int nrQueues;
    private int maxClients;
    private List<Thread> threads=new ArrayList<Thread>();
     List<Queue> queues=new ArrayList<Queue> ( );
    public Scheduler(int nrQueues,int maxClients){
        //initialising and starting the queues
        this.nrQueues=nrQueues;
        this.maxClients=maxClients;
        for (int i=1;i<=nrQueues;i++) {
            queues.add(new Queue ( i,maxClients));
        }
        for (Queue queue:queues) {
            threads.add ( new Thread ( queue ) );
        }
        for (Thread thread:threads) {
            thread.start ();
        }
    }
    public int dispatchTask(Client client){
            Queue queueMin = queues.get ( 0 );//for the queue with the least amount of time
            for (Queue queue : queues) {
                if (queue.waitingPeriod < queueMin.waitingPeriod)
                    queueMin = queue;
            }
            //the time at which the client will leave the queue
            int clientDone = queueMin.waitingPeriod + client.service + client.arrival+1;
            queueMin.addClient ( client );//add the client to the queue with the least waiting time
            return clientDone;
    }
    void modifyStop(int stop){
        for (Queue queue:queues) {
            queue.modifyStop ( stop );
        }
    }
}
