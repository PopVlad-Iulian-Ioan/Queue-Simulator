package Assignment2;

import java.io.PrintWriter;
import java.util.*;

public class SimulationManager implements Runnable {
    int nrClients,queues,simTime,minArrival=0,maxArrival=0,minService=0,maxService=0;
    private List<Client> clients=new ArrayList<Client> (  ) ;
    private Scheduler scheduler;
    private PrintWriter out;
    private int timeToServeAllClients=0;
  public SimulationManager(Scanner inSc,PrintWriter out){
        String s;
        this.out=out;
        nrClients=inSc.nextInt ();
        queues=inSc.nextInt ();
        simTime=inSc.nextInt ();
        //get minArrival and maxArrival
        s=inSc.next ();
        int i=0;
        while (s.charAt ( i )!=','){
            minArrival=minArrival*10 +s.charAt ( i )-48;
            i++;
        }
        i++;
        while (i<s.length ()){
            maxArrival=maxArrival*10 +s.charAt ( i )-48;
            i++;
        }
        //get minService and maxService
        s=inSc.next ();
        i=0;
        while (s.charAt ( i )!=','){
            minService=minService*10 +s.charAt ( i )-48;
            i++;
        }
        i++;
        while (i<s.length ()){
            maxService=maxService*10 +s.charAt ( i )-48;
            i++;
        }
        scheduler=new Scheduler ( queues,nrClients);
         generateClients ();
    }
    //random generate clients
    private void generateClients(){
      for(int i=1;i<=nrClients;i++){
          //add them to the list
          clients.add ( new Client ( i,minArrival,maxArrival,minService,maxService ) );
      }
      //sort the list
        Collections.sort ( clients,new SortByArrival() );
      //the initial time to serve all clients will be calculated as last's element arrival + serving time
      timeToServeAllClients=clients.get ( clients.size ()-1 ).arrival+clients.get ( clients.size ()-1 ).service;

  }
    @Override
    public void run() {
        synchronized (scheduler.queues) {
            int currTime = 0, clientsServed = 0, waitSum = 0,begin=0;
            while (currTime < simTime && currTime<timeToServeAllClients) {
                out.println ( "Time " + currTime );
                out.print ( "Waiting clients:" );
                //verify all the clients that were not introduced in a queue
                for(int i=begin;i<clients.size ();i++) {
                    if (currTime == clients.get ( i ).arrival) {
                        begin++;//to see how may clients that were introduced in a queue
                        int clientDone = scheduler.dispatchTask ( clients.get ( i ) );
                        if (clientDone > timeToServeAllClients)
                            //to see it the time taken to serve all the clients has modified based on the current state of the queues
                            timeToServeAllClients = clientDone;
                        clientsServed++;
                    }
                }
                try {
                    Thread.sleep ( 10 );
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
                //print the waiting clients
                for(int i=begin;i<clients.size ();i++)
                    out.print ( clients.get ( i ).printClient ());
                out.println ();
                //print the queue state
                for (Queue queue : scheduler.queues) {
                    out.println ( queue.printState () );
                }
                currTime++;
            }
            //compute the average
            for (int i=0;i<begin;i++)
                waitSum = waitSum + clients.get (i).timeSpent;;
            out.print ( "Average time: " + (float) +waitSum / clientsServed );
            //stop the queues that are still waiting
            scheduler.modifyStop ( 1 );
            out.close ();
        }
    }
}

