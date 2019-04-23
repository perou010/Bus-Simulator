public class PassengerEvent implements IEvent{

    public int currentBusStop;

    //there will need to be 10 different passenger arrival events instances created initially

    public PassengerEvent(int currentBusStop){
        this.currentBusStop = currentBusStop;
    }

    public int timeRandomizer(int averageArrivalInterval){
        int timeForNextEvent = 0;
        int random = (int) (Math.random() * 20);
        if(random < 2){timeForNextEvent = ((int) (averageArrivalInterval * 0.75)) + averageArrivalInterval;}
        else if(random < 5){timeForNextEvent = ((int) (averageArrivalInterval * 0.50)) + averageArrivalInterval;}
        else if(random < 9){timeForNextEvent = ((int) (averageArrivalInterval * 0.20)) + averageArrivalInterval;}
        else if(random < 11){timeForNextEvent = averageArrivalInterval;}
        else if(random < 15){timeForNextEvent = ((int) (averageArrivalInterval * -0.20)) + averageArrivalInterval;}
        else if(random < 18){timeForNextEvent = ((int) (averageArrivalInterval * -0.50)) + averageArrivalInterval;}
        else if(random < 20){timeForNextEvent = ((int) (averageArrivalInterval * -0.75)) + averageArrivalInterval;}
        return  timeForNextEvent;
    }

    public void run() {
        Passenger p = new Passenger(currentBusStop);

        if(p.direction){BusSim.currentSim.stops[currentBusStop].eastboundQ.add(p);}
        else if (!p.direction){BusSim.currentSim.stops[currentBusStop].westboundQ.add(p);}

        if(currentBusStop == 7 || currentBusStop == 8 || currentBusStop == 9){
            BusSim.currentSim.agenda.add(new PassengerEvent(currentBusStop), timeRandomizer(BusSim.currentSim.load * 2 / 3)); // 1/80 freq is 50% more than 1/120 freq
        } else{
            BusSim.currentSim.agenda.add(new PassengerEvent(currentBusStop), timeRandomizer(BusSim.currentSim.load));
        }
        //System.out.println("PASSENGER EVENT: StopNumber: "+currentBusStop+" Current Time: "+BusSim.currentSim.agenda.getCurrentTime());
    }

    public String toString(){
        return "Passenger Arrives @Stop "+currentBusStop+" @Time: "+BusSim.currentSim.agenda.getCurrentTime();
    }

}

