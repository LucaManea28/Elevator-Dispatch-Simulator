package com.luca.elevatorsimweb.Scheduler;

import com.luca.elevatorsimweb.domain.Direction;
import com.luca.elevatorsimweb.domain.Elevator;
import com.luca.elevatorsimweb.domain.ElevatorState;

import java.util.List;

public class SimpleScheduler implements Scheduler{
    public Elevator chooseElevator(int callFloor, Direction callDir, List<Elevator> elevators){
        Elevator best = null;
        int bestScore = Integer.MAX_VALUE;
        for(Elevator e : elevators){
            int score = scoreElevator(e, callFloor, callDir);
            if(score < bestScore){
                bestScore = score;
                best = e;
            }
        }
        return best;
    }

    private int scoreElevator(Elevator e, int callFloor, Direction callDir){
        int distance = Math.abs(e.getCurrentFloor() - callFloor);
        //daca e IDLE => e ok, nu am penalizare
        //daca e in misicare in ac. directie si vine spre cerere => mic bonus
        //altfel penalizare
        int score = distance;
        if(e.getState() == ElevatorState.IDLE)
            score -= 3;
        boolean sameDir;
        sameDir = e.getDirection() == callDir;
        boolean comingToward =
                (callDir == Direction.UP && e.getCurrentFloor() <= callFloor) ||
                        (callDir == Direction.DOWN && e.getCurrentFloor() >= callFloor);
        if(sameDir && comingToward)
            score -= 5;  //bun
        boolean moving = (e.getState() == ElevatorState.MOVING_UP || e.getState() == ElevatorState.MOVING_DOWN);
        if(moving && !(sameDir && comingToward)){
            score += 10; //penalizare
        }
        if(score < 0)
            score = 0;
        return score;
    }

    public String name(){
        return "Simple Scheduler";
    }
}
