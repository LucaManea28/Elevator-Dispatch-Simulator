package com.luca.elevatorsimweb.web;

import com.luca.elevatorsimweb.metrics.Metrics;
import com.luca.elevatorsimweb.simulation.NegativeNumber;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SimulationController {
    private final SimulationService service;
    public SimulationController(SimulationService service) {
        this.service = service;
    }

    @GetMapping("status")
    public List<ElevatorDTO> status() {
        return service.getStatus();
    }

    @GetMapping("/metrics")
    public MetricsDTO metrics(){
        return service.getMetricsDTO();
    }

    @PostMapping("/reset")
    public void reset(){
        service.reset();
    }

    @PostMapping("/go")
    public void go(@RequestBody GoRequest req){
        service.go(req.elevatorId, req.targetFloor);
    }

    @PostMapping("/call")
    public void call(@RequestBody CallRequest req){
        service.call(req.floor, req.direction, req.scheduler);
    }

    @PostMapping("/test")
    public void test(@RequestBody TestRequest req) throws NegativeNumber {
        service.startTest(req.scheduler, req.rate, req.seconds);
    }
}
