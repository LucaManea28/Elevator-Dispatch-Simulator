package com.luca.elevatorsimweb.simulation;

import com.luca.elevatorsimweb.domain.Building;
import com.luca.elevatorsimweb.domain.Direction;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class TrafficGenerator {
    private final Building building;
    private final Random rnd = new Random();

    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread worker;

    public TrafficGenerator(Building building){
        this.building = building;
    }

    public boolean isRunning(){
        return running.get();
    }

    //ratePerSecond = cereri/sec
    //onExternalCall = callback cate logica de call

    public void start(int ratePerSecond, ExternalCallHandler onExternalCall) throws NegativeNumber {
        if(ratePerSecond <= 0)
            throw new NegativeNumber("rate per second must be greater than zero");
        if(running.get()) return;

        running.set(true);
        long intervalMs = 1000L / ratePerSecond;

        worker = new Thread(()->{
            while(running.get()){
                int floor = rnd.nextInt(building.getFloor());
                Direction dir = randomValidDirection(floor, building.getFloor());
                onExternalCall.handle(floor, dir);
                try{
                    Thread.sleep(intervalMs);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        worker.start();
    }

    public void stop(){
        running.set(false);
        if(worker != null) worker.interrupt();
    }

    private Direction randomValidDirection(int floor, int totalFloor){
        int top = totalFloor - 1;
        if(floor == 0) return Direction.UP;
        if(floor == top) return Direction.DOWN;
        return rnd.nextBoolean() ? Direction.UP : Direction.DOWN;
    }

    public interface ExternalCallHandler {
        void handle(int floor, Direction dir);
    }

}
