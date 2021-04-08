package Assignment2;

import java.util.Random;

public class Client {
    int timeSpent=0;
    int id;
    int arrival;
    int service;
    Client(int id,int arrival,int service) {
        this.id=id;
        this.arrival=arrival;
        this.service=service;
    }
    //constructor for generating a random client
    Client(int id,int minArrival,int maxArrival,int minService,int maxService){
        Random random=new Random (  );
        this.id=id;
        arrival=random.nextInt (maxArrival-minArrival)+minArrival;
        service=random.nextInt (maxService-minService)+minService;
    }
    public String printClient(){
        return "("+id+","+arrival+","+service+"); ";
    }
}
