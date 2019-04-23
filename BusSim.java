public class BusSim {

    public static BusSim currentSim; // current instance of BusSim, reset+parameters altered in main function

    public Statistics stat;
    public PQ agenda;
    public BusStop[] stops = {new BusStop("University Ave and 27th Street SE"), new BusStop("Raymond Ave Station"), new BusStop("University Ave and Fairview Ave"), new BusStop("University Ave and Snelling Ave"), new BusStop("University Ave and Lexington Ave"), new BusStop("University Ave and Dale Street"), new BusStop("University Ave and Marion Street"), new BusStop("Cedar Street and 5th Street"), new BusStop("Minnesota Street and 4th Street"), new BusStop("Union Depot")};
    public int load; //avg seconds between passenger arrival at non-downtown stops
    public int numberOfBuses;
    public Bus[] currentBuses; //used in determine clumping
    public int largeBuses; // 0 => no large buses 1 => ~half large buses 2 => all large buses

    public BusSim(int load, int numberOfBuses, int largeBuses){
        stat = new Statistics();
        agenda = new PQ();
        this.load = load;
        this.numberOfBuses = numberOfBuses; // 1-18
        this.largeBuses = largeBuses; // 0,1,2
    }

    public double[] runSim() { // effectively the main function for the simulation
        // add the 10 Passenger events to the agenda - they will be made to automatically reschedule themselves
        PassengerEvent[] eventArray = new PassengerEvent[10];
        for(int i = 0; i < 10; i++){
            eventArray[i] = new PassengerEvent(i);
            agenda.add(eventArray[i], 10);
        }

        // variable number and size of buses
        currentBuses = new Bus[numberOfBuses];
        for (int i = 0; i < numberOfBuses; i++) {
            int[][] initializeArray = {{0,1},{9,0},{4,1},{5,0},{2,0},{7,1},{3,1},{6,0},{1,1},{8,0},{4,0},{5,1},{2,1},{7,0},{1,0},{8,1},{3,0},{6,1}}; // makes sure added buses are reasonably spaced out
            if(largeBuses == 0){
                currentBuses[i] = new Bus(40, initializeArray[i][1] == 1 ? true : false);
                agenda.add(new BusEvent(initializeArray[i][0], currentBuses[i]), 10);
            } else if(largeBuses == 1){
                if(i % 2 == 0){
                    currentBuses[i] = new Bus(40, initializeArray[i][1] == 1 ? true : false);
                    agenda.add(new BusEvent(initializeArray[i][0], currentBuses[i]), 10);
                } else if(i % 2 == 1){
                    currentBuses[i] =new Bus(60, initializeArray[i][1] == 1 ? true : false);
                    agenda.add(new BusEvent(initializeArray[i][0], currentBuses[i]), 10);
                }
            } else if(largeBuses == 2){
                currentBuses[i] =new Bus(60, initializeArray[i][1] == 1 ? true : false);
                agenda.add(new BusEvent(initializeArray[i][0], currentBuses[i]), 10);

            }
        }

        while(agenda.getCurrentTime() <= 100000){
            IEvent event = agenda.remove();
//            if(!(event instanceof PassengerEvent)){
//                System.out.println(event);
//                for (int i = 0; i < currentBuses.length; i++) {
//                    System.out.println(currentBuses[i].currentStop+""+(currentBuses[i].direction == true ? " ->" : " <-"));
//                }
//            }// uncomment to print every event the occurs
            event.run();
        }
        // Uncomment to Display those left in queues at end of simulation
//        for (int i = 0; i < 10; i++) {
//            if(currentSim.stops[i].eastboundQ != null){System.out.println("Eastbound at stop "+i+": "+currentSim.stops[i].eastboundQ.length());}
//            if (currentSim.stops[i].westboundQ != null) {System.out.println("Westbound at stop "+i+": "+currentSim.stops[i].westboundQ.length());}
//        }
        // uncomment to print stats
//        System.out.println(stat.avgTravelTime());
//        System.out.println(stat.pmpg()+"\n");
        double[] returnArray = {stat.avgTravelTime(), stat.pmpg()};
        return returnArray;
    }

    public static double netRating(double averageTravelTime, double pmpg){
        double pmpgRating = ((pmpg - 5) * (100)) / 295;
        double averageTravelTimeRating = -Math.exp((Math.log(100))/(10000) * (averageTravelTime))+ 100;
        return pmpgRating + averageTravelTimeRating / 2;
    }

    public static double[] runABunchOfSimulations(int load){
        // 54 potential combinations - which is best for each arrival interval?
        double[] currentSimResults;
        double[] ratingArray = new double[54];
        double[][] allResults = new double[54][];
        int count = 0;

        for (int j = 0; j < 3; j++) {
            for (int k = 1; k < 19; k++) {
                currentSim = new BusSim(load, k, j);
                currentSimResults = currentSim.runSim();
                allResults[count] = new double[] {currentSimResults[0], currentSimResults[1], j, k};
                count++;
            }
        }

        for (int i = 0; i < allResults.length; i++) {
            ratingArray[i] = netRating(allResults[i][0], allResults[i][1]);
        }

        // search for best one
        double bestRating = 0;
        int bestRatingIndex = -1;
        for (int i = 0; i < ratingArray.length; i++) {
            if(ratingArray[i] > bestRating){
                bestRating = ratingArray[i];
                bestRatingIndex = i;
            }
        }
        return new double[] {load, allResults[bestRatingIndex][2], allResults[bestRatingIndex][3]};
    }

    public static void main(String[] args) {
        int load = 20; // load in (10, 250) //change load (avg period of passenger arrival at each stop (more frequent at downtown stops)) here
        double[] data;
        data = runABunchOfSimulations(load);
        System.out.println("IDEAL VALUES FOR LOAD = "+(int) data[0]+"\n");
        System.out.println("\tNumber of Buses: "+(int) data[2]);
        System.out.println("\tLarge Buses: "+(int) data[1]+"\n");
        System.out.println("\tLarge Buses Key: \n\t\t0: all small buses \n\t\t1: half small, half large (odd number of buses => one less large than small)\n\t\t2: all large buses");
    }
}
