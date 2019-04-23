import java.util.ArrayList;
import java.util.Stack;

public class Bus {

    boolean direction; //true => eastbound , false => westbound
    int currentStop = -1; // will be updated when new BusEvents are created
    int numberOfPassengers = 0;
    int size;
    ArrayList<Stack<Passenger>> passengers = new ArrayList<>();
    

    public Bus(int size, boolean direction){
        for(int i = 0; i < 10; i++){ // ten stacks: one for each dropOff point
            passengers.add(new Stack<Passenger>());
        }
        this.size = size;
        this.direction = direction;
    }

    public boolean add(Passenger passenger){
        if(numberOfPassengers == size){return false;}
        passengers.get(passenger.dropoffStop).push(passenger);
        numberOfPassengers++;
        return true;
    }

    public String toString(){
        return passengers.toString();
    }
}
