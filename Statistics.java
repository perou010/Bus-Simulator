// written by perou010

public class Statistics {

    private double[] statsArray;

    public Statistics(){
        statsArray = new double[6];// 0totalTravelTime, 1timesRun, 2passengersOnSmallBus, 3passengersOnLargeBus, 4timesRunSmall, 5timesRunLarge,
    }

    public double[] getStatsArray(){return statsArray;}

    public double avgTravelTime(){return statsArray[0] / statsArray[1];}

    public double pmpg(){
        if(BusSim.currentSim.largeBuses == 0){
            return 6 * (statsArray[2] / statsArray[4]);
        } else if(BusSim.currentSim.largeBuses == 1){
            if (BusSim.currentSim.numberOfBuses == 1){ // divide by zero would happen otherwise since no large buses
                return 6 * (statsArray[2] / statsArray[4]);
            }
            return ((6 * (statsArray[2] / statsArray[4])) + (4 * (statsArray[3] / statsArray[5]))) / 2;
        } else{ // if BusSim.currentSim.largeBuses == 2
            return 4 * (statsArray[3] / statsArray[5]);
        }
    }
}

