public class PQ implements IPQ {

    public PQ() {
        seg = new Segment(0);
    }

    public void add(IEvent o, double time) {

        time += getCurrentTime();
        
        if (time < seg.getTime())
          System.out.println("Error: trying to go back in time");
        else if (time == seg.getTime())
          seg.getEvents().add(o);
        else {  // search list for correct insertion point, then insert
          Segment trailer = seg, ptr = seg.getNext();
          while (ptr != null && time > ptr.getTime()) { //search
              ptr = ptr.getNext();
              trailer = trailer.getNext();
          }  // search
          if (ptr != null && time == ptr.getTime())
            ptr.getEvents().add(o);
          else {  // add new segment after trailer and before ptr
            Segment temp = new Segment(time);
            temp.getEvents().add(o);
            temp.setNext(ptr);
            trailer.setNext(temp); 
          }  // add new segment after trailer and before ptr
        }  // search list for correct insertion point, then insert
    }  // add method

    public IEvent remove() {
        if (this.isEmpty()) {
          System.out.println("Error: removing from empty queue");
          return null;
        }
        else if (seg.getEvents().length() == 0) {
          seg = seg.getNext();        
          return (IEvent) seg.getEvents().remove();
        }
        else return (IEvent) seg.getEvents().remove();
    }

    public boolean isEmpty() {
        return (seg.getEvents().length() == 0 && seg.getNext() == null);
    }

    public double getCurrentTime() { 
        return seg.getTime();
    }


    public Segment seg;  // front of list representing priority queue

}
