package com.luca.elevatorsimweb.Scheduler;
import com.luca.elevatorsimweb.domain.Direction;
import com.luca.elevatorsimweb.domain.Elevator;
import java.util.*;

public interface Scheduler {
    Elevator chooseElevator(int callFloor, Direction callDir, List<Elevator> elevators);
    String name();
}
