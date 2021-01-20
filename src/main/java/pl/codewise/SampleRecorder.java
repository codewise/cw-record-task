package pl.codewise;

import java.util.UUID;

public interface SampleRecorder {

    void record(UUID campaignId, double cost);

    double getAverage(UUID campaignId);
}
