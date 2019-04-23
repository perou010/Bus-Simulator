public class BusStop {

    static int stopsCreated = 0;
    Q eastboundQ = new Q();
    Q westboundQ = new Q();
    String name;
    // corresponds to it's index in the array
    // will be used to access busStops as opposed to by name
    int stopNumber;

    public BusStop(String name){
        this.name = name;
        stopNumber = stopsCreated;
        if (stopNumber == 0){
            westboundQ = null;
        } else if(stopNumber == 9){
            eastboundQ = null;
        }
        stopsCreated++;
    }

    public String toString(){
        return "Stop: "+stopNumber+" "+this.name;
    }
}
