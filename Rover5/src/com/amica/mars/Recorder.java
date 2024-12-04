package com.amica.mars;

import java.util.ArrayList;
import java.util.List;

public class Recorder implements Telemetry {

    private List<Report> reports;

    public List<Report> getReports() {
        return reports;
    }

    public Recorder() {
        reports = new ArrayList<>();
    }

    public void sendMessage(Report report) {
        reports.add(report);
    }
}
