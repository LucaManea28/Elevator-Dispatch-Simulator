package com.luca.elevatorsimweb.Scheduler;

import com.luca.elevatorsimweb.domain.Direction;
import com.luca.elevatorsimweb.domain.Elevator;
import com.luca.elevatorsimweb.domain.ElevatorState;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;

public class QueueAwareScheduler implements Scheduler{
    private final int queuePenalty;

    public QueueAwareScheduler(int queuePenalty){
        this.queuePenalty = queuePenalty;
    }

    public Elevator chooseElevator(int callFloor, Direction callDir, List<Elevator> elevators){
        Elevator best = null;
        int bestScore = Integer.MAX_VALUE;
        for(Elevator elevator : elevators){
            int score = scoreElevator(elevator, callFloor, callDir);
            if(score < bestScore){
                bestScore = score;
                best = elevator;
            }
        }
        return best;
    }

    private int scoreElevator(Elevator e, int callFloor, Direction callDir){
        int distance = Math.abs(e.getCurrentFloor() - callFloor);
        int queue = e.stopsCount();
        int score = distance + queuePenalty * queue;
        //practic un lift care are multe opriri are sanse mai mici sa fie ales
        //vreau sa am cat mai multe lifturi active
        if(e.getState() == ElevatorState.IDLE)
            score -= 3;
        boolean sameDir = e.getDirection() == callDir;
        boolean comingToward =
                (callDir == Direction.UP && e.getCurrentFloor() <= callFloor) ||
                        (callDir == Direction.DOWN && e.getCurrentFloor() >= callFloor);
        if(sameDir && comingToward){
            score -= 5;
        }
        if(score < 0)
            score = 0;
        return score;
    }

    public String name(){
        return "Queue Scheduler";
    }
}
