package pl.codewise;

import java.lang.Double;
import java.util.*;

import pl.codewise.SampleRecorder;

/**
 * Implementation of the SampleRecorder interface
 *
 * The class records samples of the type (id, cost) denoting spending the value of cost on the campaign with the given
 * id, and at any moment can compute the average cost in the last N samples with the given id. (If there were less than
 * N samples with the given id, the average of all samples with the given id is returned.)
 * The value of N is specified in the constructor.
 *
 * For each campaign, instead of storing the sequence of amounts spent on that id, we store the prefix sums of that
 * sequence. In other words, when recording a sample, we append to the sequence for that id the total sum of
 * costs spent on the campaign with the given id. For each campaign we store only (N+1) most recent sums.
 * This way we can compute the average amount spent on the campaign in O(1) as (last - first)/(length - 1).
 * where last denotes the oldest stored sum, first denotes the most recent stored value, and length denotes the number
 * of stored sums (length <= N + 1).
 */
public class SampleRecorderImpl implements SampleRecorder {

    private int maxEventsPerCampaign;
    private HashMap<UUID, Deque<Double>> events = new HashMap<>();
    public SampleRecorderImpl(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("The value of N is not positive");
        }
        this.maxEventsPerCampaign = N;
    }
    @java.lang.Override
    public void record(UUID campaignId, double cost) {
        events.putIfAbsent(campaignId, new LinkedList<Double>(Collections.singleton(0.)));
        var deque = events.get(campaignId);
        double oldSum = deque.getLast();
        if (deque.size() == maxEventsPerCampaign + 1) deque.removeFirst();
        deque.addLast(oldSum + cost);
    }

    @java.lang.Override
    public double getAverage(UUID campaignId) {
        var deque = events.get(campaignId);
        if (deque == null) {
            throw new NoSuchElementException("No campaign with the given id found");
        }
        return (deque.getLast() - deque.getFirst()) / (deque.size() - 1);

    }




}
