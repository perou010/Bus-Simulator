============
Project Info
============

Project Name: Bus Simulation
Author: Ryan Peroutka (perou010)

==========
How To Use
==========
1. Place all .java files in BusSim.tar into new directory.

2. Open BusSim.java, edit line 114 (first line of main function) to desired load value (currently set to 120) and save. 
  
3. Compile BusSim.java.

4. Run BusSim.

5. Printed output reveals the ideal value for number of buses (1 - 18) and whether all small buses (0), all large buses (2), or a 50/50 split (1) is best.

=====================
Organization Overview
=====================
-----------------------------
Stuff for PQ (Q, PQ, Segment)
-----------------------------
Taken from class website. The agenda is a PQ. It relies on Q and Segment.

------------------------------------------
Elements (Passenger, Bus, BusStop)
------------------------------------------
Passengers are passive objects. Their destination is determined at creation (won't be their current position). 
Buses have an ArrayList of Stacks of passengers (one stack per BusStop in existence). Its add method add a passenger to the appropriate stack and keeps track of how full the bus is (won't allow adding if bus is full).
BusStops have two queues for Passengers (the edges one have one). Each stop has a number 0-9 associate with it (0 -> westmost, 9 ->eastmost).

---------------------------------
Events (PassengerEvent, BusEvent)
---------------------------------
A PassengerEvent occurring represents a new passenger showing up to a bus stop. An instance of PassengerEvent is initially created at each BusStop. Each one will continually reschedule itself. The timeRandomizer function within is used to determine the time of the next occurence. Downtown stops are more active than others. Destination of Passenger is determined in Passenger class. Passenger is added to appropriate (either eastbound or westbound queue).
A BusEvent occurs every time a bus shows up at a stop. It also is used to make a bus wait at least 15 seconds at each stop. First, the stack of passengers destined for this stop is emptied. Next, passengers are loaded from the correct queue until either the bus is full or the queue is empty (if the bus hasn't waited 15 seconds, it will reschedule itself at the same stop 15-waitTime into the future). After waiting (if necessary) it will schedule itself for the next stop on the route. Before the next (non-waiting) bus event is scheduled, BusEvent.run() checks to see if there are one or more buses at the next stop in line going the direction the current bus will be going. If so, the next BusEvent for this bus is scheduled for precisely 45 seconds longer into the future than it would have been otherwise. I determined 45 seconds to be the value that best reduces instances of clumping (from ~1000/sim mostly occurring towards the tail end to <200 with a much more spread out distribution. 

-----------------------------
Stuff for PQ (Q, PQ, Segment)
-----------------------------
Taken from class website. The agenda is a PQ. It relies on Q and Segment.

--------------------------
Other (Statistics, BusSim, netRating())
--------------------------
An instance of Statistics is created for each instance of BusSim. The values need to compute the desired statistics are continuously updated in each BusEvent (Passenger related statistics are taken care of at bus deloading). Creating individual instances as opposed to having a bunch of static variables makes computing statistics on multiple simulations, one after the other, much simpler. 

***Important*** An instance of BusSim is created for each simulation. As such, the main() function in BusSim is not where the main while loop that drives the simulation is stored (like in the lab / class examples). This is held in the (non-static) runSim() function. This decision was made so that the simulations with different variable can easily be run multiple times. This decision allowed me to run the 54 possible combinations of the number of buses and the proportion of large buses and determine the best combination for a give load value. 

***Important*** The static netRating() function within BusSum is an interesting component. For each statistic, a bijective mapping from the statistic value to (roughly) the interval (0,100). For pmpg (line 76), this mapping is linear. Larger value of pmpg lead to larger values of this rating. For avgTravelTime(line 77), the mapping utilizes the exponential operator. The result is the shaving time off an extremely long ride is increasingly more valuable than shaving time of a short ride. (people aren't usually concerned about how long the ride was if it was quick, but as the ride length get excessive, each passing second will make them increasingly more dissatisfied).
After this, the two standardized ratings are combined to create a final net rating. 

Within runABunchOfSimulations() and main(), the variables corresponding to the best net rating for a given load value are retrieved and presented to the user. 

====================
Data Structures and Algorithms Used
====================
-----
Queue
-----
Each BusStop has two (only one for the edges) queues associated with it to release passengers onto buses according to their order of arrival. 

--------------
Priority Queue
--------------
At the heart of the simulation, the priority queue (agenda), is used to keep track of every event (PassengerEvents and BusEvents). It's the natural choice for discrete event simulation.
  
-----
Stack
-----
Each bus has 10 stacks within it - one for each stop that passengers could be headed to. A stack is not the only data structure that would work in this situation. Pushing and popping without having to account for indices makes this more convenient than an ArrayList.

----------
Array List
----------
Java's ArrayList is used to store all the stacks on each bus. An array would work just as well in theory, however, creating a generic array of stacks is not an option in java.  

============
Known Issues
============

I am not aware of any issues within the simulation code. 

