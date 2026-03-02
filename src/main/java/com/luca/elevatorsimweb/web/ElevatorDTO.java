package com.luca.elevatorsimweb.web;

public class ElevatorDTO {
    public int id;
    public int currentFloor;
    public String state;
    public String direction;

    public ElevatorDTO(int id, int currentFloor, String state, String direction) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.state = state;
        this.direction = direction;
    }
}
