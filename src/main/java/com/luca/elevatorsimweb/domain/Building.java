package com.luca.elevatorsimweb.domain;

import com.luca.elevatorsimweb.metrics.Metrics;

import java.util.ArrayList;
import java.util.List;

public class Building {
    private final int floors;
    private final List<Elevator> elevators;

    public Building(int floors, int elevatorCount, Metrics metrics) {
        this.floors = floors;
        this.elevators = new ArrayList<>();
        for (int i = 0; i < elevatorCount; i++) {
            elevators.add(new Elevator(i + 1, 0, metrics));
        }
    }

    public int getFloors(){
        return floors;
    }

    public List<Elevator> getElevators(){
        return elevators;
    }

    public int getFloor(){
        return floors;
    }

    public void tickAll(){
        for(Elevator e : elevators){
            e.tick();
        }
    }
}
