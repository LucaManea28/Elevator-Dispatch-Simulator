package com.luca.elevatorsimweb.web;

import com.luca.elevatorsimweb.domain.Direction;

public class CallRequest {
    public int floor;
    public Direction direction;
    public String scheduler;
    public CallRequest(){};
}
