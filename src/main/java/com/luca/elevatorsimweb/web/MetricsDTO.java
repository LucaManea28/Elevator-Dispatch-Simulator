package com.luca.elevatorsimweb.web;

public class MetricsDTO {
    public long totalRequests;
    public long servicedRequests;
    public long totalWaitMs;
    public double averageWaitMs;

    public MetricsDTO(long totalRequests, long servicedRequests, long totalWaitMs, double averageWaitMs) {
        this.totalRequests = totalRequests;
        this.servicedRequests = servicedRequests;
        this.totalWaitMs = totalWaitMs;
        this.averageWaitMs = averageWaitMs;
    }
}
