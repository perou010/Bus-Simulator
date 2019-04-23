public class Passenger {

    double timeArrived;
    int initialStop; //**
    int dropoffStop;
    boolean direction; // true => eastbound , false => westbound

    public Passenger(int initialStop){
        this.initialStop = initialStop; // ** this might be needed for some statistics. toString also relies.
        timeArrived = BusSim.currentSim.agenda.getCurrentTime();
        //choose a new destination randomly
        dropoffStop = (int) (Math.random() * 13);
        while (initialStop == dropoffStop){
            dropoffStop = (int) (Math.random() * 13);
        }
        // account for downtown stops being more popular. crude, but it works.
        if(dropoffStop == 10){
            dropoffStop = 7;
        } else if(dropoffStop == 11){
            dropoffStop = 8;
        } else if(dropoffStop == 12){
            dropoffStop = 9;
        }
        // determine direction
        if(dropoffStop < initialStop){direction = false;}
        else if(dropoffStop > initialStop){direction = true;}
    }

    public String toString(){
        String s = "PASSENGER: Destination: "+dropoffStop+" Initial: "+initialStop+" Direction: ";
        if(direction){s+="E->";}
        else if(!direction){s+="<-W";}
        return "\n" + s + "\n";
    }
}
