package com.luca.elevatorsimweb.domain;
import com.luca.elevatorsimweb.metrics.Metrics;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.Map;

public class Elevator {
    private final int id;
    private int currentFloor;
    private Metrics metrics;

    private ElevatorState state = ElevatorState.IDLE;
    private Direction direction = Direction.UP;

    private final TreeSet<Integer> upStops = new TreeSet<>();
    private final TreeSet<Integer> downStops = new TreeSet<>();
    private final Map<Integer, Long> requestTimes = new HashMap<>();

    private int doorTicksRemaining = 0;

    public Elevator(int id, int floor, Metrics metrics){
        this.id = id;
        this.currentFloor = floor;
        this.metrics = metrics;
    }

    public void addStop(int floor){
        long now = System.currentTimeMillis();
        if(floor == currentFloor && state != ElevatorState.MOVING_DOWN && state != ElevatorState.MOVING_UP) {
            openDoor(3);
            return;
        }
        if(floor > currentFloor) {
            upStops.add(floor);
        }else if(floor < currentFloor){
            downStops.add(floor);
        }
        requestTimes.put(floor, now);
        if(state == ElevatorState.IDLE){
            decideInitialDirection();
        }
    }

    public void tick(){
        if(state == ElevatorState.DOOR_OPEN){
            doorTicksRemaining--;
            if(doorTicksRemaining <= 0){
                state = ElevatorState.IDLE;
                decideInitialDirection();
            }
            return;
        }
        Integer next = nextStop();
        if(next == null){
            state = ElevatorState.IDLE;
            return;
        }
        if(next > currentFloor){
            direction = Direction.UP;
            state = ElevatorState.MOVING_UP;
            currentFloor++;
        }else if(next < currentFloor){
            direction = Direction.DOWN;
            state = ElevatorState.MOVING_DOWN;
            currentFloor--;
        }
        if(currentFloor == next){
            Long requestTime = requestTimes.remove(currentFloor);
            if(requestTime != null){
                long waitTime = System.currentTimeMillis() - requestTime;
                metrics.recordServicedRequest(waitTime);
            }
            upStops.remove(currentFloor);
            downStops.remove(currentFloor);
            openDoor(3);
        }
    }

    private void openDoor(int ticks){
        state = ElevatorState.DOOR_OPEN;
        doorTicksRemaining = ticks;
    }

    public void decideInitialDirection(){
        if(!upStops.isEmpty() && !downStops.isEmpty()) {
            //aleg cea mai apropiata oprire
            int nearestUp = upStops.first();
            int nearestDown = downStops.last();
            int distUp = Math.abs(nearestUp - currentFloor);
            int distDown = Math.abs(nearestDown - currentFloor);
            if (distUp <= distDown) {
                direction = Direction.UP;
                state = ElevatorState.MOVING_UP;
            } else {
                direction = Direction.DOWN;
                state = ElevatorState.MOVING_DOWN;
            }
            return;
        }
        if(!upStops.isEmpty()) {
            direction = Direction.UP;
            state = ElevatorState.MOVING_UP;
            return;
        }
        if(!downStops.isEmpty()) {
            direction = Direction.DOWN;
            state = ElevatorState.MOVING_DOWN;
        }
    }

    private Integer nextStop(){
        if(direction == Direction.UP){
            Integer above = upStops.ceiling(currentFloor + 1);
            if(above != null) return above;

            Integer below = downStops.floor(currentFloor - 1);
            if(below != null){
                direction = Direction.DOWN;
                return below;
            }
            return null;
        }else{
            Integer below = downStops.floor(currentFloor - 1);
            if(below != null) return below;

            Integer above = upStops.ceiling(currentFloor + 1);
            if(above != null){
                direction = Direction.UP;
                return above;
            }
            return null;
        }
    }

    public int stopsCount(){
        return upStops.size() + downStops.size();
    }

    public int getCurrentFloor(){
        return currentFloor;
    }

    public ElevatorState getState(){
        return state;
    }

    public Direction getDirection(){
        return direction;
    }

    public int getId(){
        return id;
    }

    public String debugStops(){
        return "up = " + upStops + " down = " + downStops;
    }
}
