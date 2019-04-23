import java.util.Stack;

public class BusEvent implements IEvent{

    private Bus bus;
    private int currentBusStop;
    int waitTime = 0;
    boolean hasWaited = false;

    public BusEvent(int currentBusStop, Bus bus){
        this.bus = bus;
        this.currentBusStop = currentBusStop;
    }

    public boolean willClump(){
        if(BusSim.currentSim.currentBuses.length > 8){return false;} //don't check for clumping ian f >8 buses - it wouldn't make sense to try to avoid
        int numAtNextStop = 0; //this bus will NOT be counted
        //System.out.println("\nBus Being Checked: stop: "+bus.currentStop+" direction: "+(bus.direction ? "->" : "<-"));
        for (int i = 0; i < BusSim.currentSim.currentBuses.length; i++) {
            Bus checkBus = BusSim.currentSim.currentBuses[i];
            if((bus.currentStop - 1) + 2 * (bus.direction ? 1 : 0) == checkBus.currentStop){
                //System.out.println("Bus at "+((bus.currentStop - 1) + 2 * (bus.direction ? 1 : 0))+"! "+(checkBus.direction ? "->" : "<-"));
                //entering here means a bus is at the next stop - we need to check its the proper direction now.
                if((bus.currentStop - 1) + 2 * (bus.direction ? 1 : 0) == 0 || (bus.currentStop - 1) + 2 * (bus.direction ? 1 : 0) == 9){
                    if(bus.direction != checkBus.direction){
                        numAtNextStop++;
                    }
                } else{//normal case
                    if(bus.direction == checkBus.direction){
                        numAtNextStop++;
                    }
                }
            }
        }
        //System.out.println(numAtNextStop > 0);
        return false; //Uncomment this and all print functions in this method. Then ctrlF to observe the degree to which clumping is prevented. (roughly 800 instances of clumping -> ~200 if prevention is activated
        //return numAtNextStop > 0;
    }

    public void run(){
        Stack<Passenger> passengersGettingOff = bus.passengers.get(currentBusStop);
        Passenger passengerGettingOff;
        while(!passengersGettingOff.empty()){
            passengerGettingOff = passengersGettingOff.pop();
            BusSim.currentSim.stat.getStatsArray()[0] += (BusSim.currentSim.agenda.getCurrentTime() - passengerGettingOff.timeArrived);
            BusSim.currentSim.stat.getStatsArray()[1]++;
            waitTime += 2;
            bus.numberOfPassengers--;
        }
        Q<Passenger> outboundQ = null; // initialized to satisfy intellij
        if(currentBusStop == 0){bus.direction = true;}
        else if(currentBusStop == 9){bus.direction = false;}
        bus.currentStop = currentBusStop; // update bus's current stop for determining clumping
        if(bus.direction){outboundQ = BusSim.currentSim.stops[currentBusStop].eastboundQ;}
        else if(!bus.direction){outboundQ = BusSim.currentSim.stops[currentBusStop].westboundQ;}
        while(outboundQ.length() != 0 ){
            if(bus.add(outboundQ.getFront())){ // getFront does not enqueue
                outboundQ.remove(); // if add succeeds, do enqueue
                waitTime += 3;
            } else{ // meaning the bus is full
                break;
            }
        }
        if (willClump()){
            BusSim.currentSim.agenda.add(new BusEvent((currentBusStop - 1) + 2 * (bus.direction ? 1 : 0), bus), 225 + waitTime); //wait an extra 45 seconds if about to clump
        } else{ // in the case the bus wont clump
            if(waitTime < 15 && !hasWaited){
                BusEvent waitingEvent = new BusEvent(currentBusStop, bus);
                waitingEvent.hasWaited = true;
                BusSim.currentSim.agenda.add(waitingEvent, 15 - waitTime); // this should only be done once per stop
            } else{//wait time will eventually be longer than 15 seconds
                //already accounted for flipping the buses, so only one (kinda two actually) cases are needed here
                BusSim.currentSim.agenda.add(new BusEvent((currentBusStop - 1) + 2 * (bus.direction ? 1 : 0), bus), 180 + waitTime); // waitTime+3 minutes for travel time to new shop
                waitTime = 0; // reset in preparation for next stop
            }
        }
        if(bus.size == 40){
            BusSim.currentSim.stat.getStatsArray()[4]++;
            BusSim.currentSim.stat.getStatsArray()[2] += bus.numberOfPassengers;
        } else if(bus.size == 60){
            BusSim.currentSim.stat.getStatsArray()[5]++;
            BusSim.currentSim.stat.getStatsArray()[3] += bus.numberOfPassengers;
        }
    }

    public String toString(){
        if(hasWaited){return "\nBus Arrives (waiting) @Stop "+currentBusStop+" @Time: "+BusSim.currentSim.agenda.getCurrentTime()+" Direction: "+(bus.direction == true ? "->" : "<-")+"\n";}
        else {return "\nBus Arrives @Stop "+currentBusStop+" @Time: "+BusSim.currentSim.agenda.getCurrentTime()+" Direction: "+(bus.direction == true ? "->" : "<-")+"\n";}
    }
}
