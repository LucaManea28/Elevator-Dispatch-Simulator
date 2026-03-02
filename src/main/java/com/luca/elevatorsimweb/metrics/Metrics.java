package com.luca.elevatorsimweb.metrics;

public class Metrics {
    public long totalRequests = 0;
    public long servicedRequests = 0;
    public long totalWaitTime = 0;

    public synchronized void recordNewRequest() {
        totalRequests++;
    }

    public synchronized void recordServicedRequest(long waitTime) {
        servicedRequests++;
        totalWaitTime += waitTime;
    }

    public synchronized void printStats(){
        System.out.println("Elevator System Metrics: ");
        System.out.println("Total requests: " + totalRequests);
        System.out.println("Serviced requests: " + servicedRequests);
        if(servicedRequests > 0){
            System.out.println("Total waiting time: " + (totalWaitTime/servicedRequests) + "ms");
        }else{
            System.out.println("Total waiting time: N/A");
        }
    }

    public synchronized void reset(){
        totalRequests = 0;
        servicedRequests = 0;
        totalWaitTime = 0;
    }

    public long getTotalRequests() { return totalRequests; }
    public long getServicedRequests() { return servicedRequests; }
    public long getTotalWaitMs() { return totalWaitTime; } // sau cum se numește la tine
    public double getAvgWaitMs() {
        return servicedRequests == 0 ? 0.0 : (double) totalWaitTime / servicedRequests;
    }

}
