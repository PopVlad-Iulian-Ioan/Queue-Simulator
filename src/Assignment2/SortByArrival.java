package Assignment2;

import java.util.Comparator;

public class SortByArrival implements Comparator<Client> {
    @Override
    public int compare(Client client1 , Client client2) {
        return client1.arrival-client2.arrival;
    }
}
