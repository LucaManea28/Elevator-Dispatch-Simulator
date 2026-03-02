package com.luca.elevatorsimweb.web;

import com.luca.elevatorsimweb.Scheduler.QueueAwareScheduler;
import com.luca.elevatorsimweb.Scheduler.Scheduler;
import com.luca.elevatorsimweb.Scheduler.SimpleScheduler;
import com.luca.elevatorsimweb.domain.Building;
import com.luca.elevatorsimweb.domain.Direction;
import com.luca.elevatorsimweb.domain.Elevator;
import com.luca.elevatorsimweb.metrics.Metrics;
import com.luca.elevatorsimweb.simulation.NegativeNumber;
import com.luca.elevatorsimweb.simulation.TrafficGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimulationService {

    private final Metrics metrics = new Metrics();
    private final Building building = new Building(13, 3, metrics);
    private final TrafficGenerator traffic = new TrafficGenerator(building);

    private final SimpleScheduler simple = new SimpleScheduler();
    private final QueueAwareScheduler  queue = new QueueAwareScheduler(4);

    public SimulationService() {
        Thread sim = new Thread(() -> {
            while (true) {
                building.tickAll();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
        });
        sim.setDaemon(true);
        sim.start();
    }

    private synchronized void tick(){
        building.tickAll();
    }

    public synchronized List<ElevatorDTO> getStatus() {
        return building.getElevators().stream()
                .map(e -> new ElevatorDTO(
                        e.getId(),
                        e.getCurrentFloor(),
                        e.getState().toString(),
                        e.getDirection().toString()
                ))
                .collect(Collectors.toList());
    }

    public synchronized Metrics getMetrics() {
        return metrics;
    }

    public synchronized MetricsDTO getMetricsDTO() {
        return new MetricsDTO(metrics.getTotalRequests(),
                metrics.getServicedRequests(),
                metrics.getTotalWaitMs(),
                metrics.getAvgWaitMs()
                );
    }

    public synchronized void reset(){
        traffic.stop();
        metrics.reset();
    }

    public synchronized void go(int elevatorId, int targetFloor){
        Elevator e = building.getElevators().get(elevatorId - 1);
        e.addStop(targetFloor);
    }

    public synchronized void call(int floor, Direction dir, String schedulerName){
        metrics.recordNewRequest();
        Elevator chosen = pickScheduler(schedulerName).chooseElevator(floor, dir, building.getElevators());
        chosen.addStop(floor);
    }

    public void startTest(String schedulerName, int rate, int seconds) throws NegativeNumber{
        if(seconds <= 0) throw new NegativeNumber("Seconds must be positive");
        synchronized (this){
            traffic.stop();
            metrics.reset();
        }
        var scheduler = pickScheduler(schedulerName);
        traffic.start(rate, (floor, dir) -> {
                synchronized (SimulationService.this){
                    metrics.recordNewRequest();
                    Elevator chosen = scheduler.chooseElevator(floor, dir, building.getElevators());
                    chosen.addStop(floor);
                }
        });
        new Thread(() -> {
            try{
                Thread.sleep(seconds * 1000L);
            }catch(InterruptedException ignored){}
            traffic.stop();
        }).start();
    }

    private Scheduler pickScheduler(String schedulerName) {
        if (schedulerName.equals("simple"))
            return simple;
        return queue;
    }
}