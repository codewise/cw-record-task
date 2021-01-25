package pl.codewise;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.testng.Assert.*;

public class SampleRecorderImplTest {

    final static double DELTA = 0.001;

    @Test(
            expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "The value of N is not positive"
    )
    public void testNegativeN() {
        new SampleRecorderImpl(-2);
    }

    @Test(
            expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "The value of N is not positive"
    )
    public void testZeroN() {
        new SampleRecorderImpl(0);
    }

    @Test
    public void testSingleUUID() {
        var sr = new SampleRecorderImpl(3);
        var uuid = new UUID(0, 0);
        sr.record(uuid, 10.);
        assertEquals(sr.getAverage(uuid), 10., DELTA);
        sr.record(uuid, 2);
        assertEquals(sr.getAverage(uuid), 6., DELTA);
        sr.record(uuid, 3);
        assertEquals(sr.getAverage(uuid), 5., DELTA);
        sr.record(uuid, 4);
        assertEquals(sr.getAverage(uuid), 3., DELTA);
        sr.record(uuid, 20.);
        assertEquals(sr.getAverage(uuid), 9., DELTA);
    }


    @Test
    public void testMultipleUUID() {
        final int N = 3;
        var sr = new SampleRecorderImpl(N);

        final var uuid1 = new UUID(0, 0);
        final var uuid2 = new UUID(0, 1);
        final var uuid3 = new UUID(0, 2);

        sr.record(uuid1, 10.);
        sr.record(uuid1, 11.);
        sr.record(uuid2, 20.);
        sr.record(uuid3, 30.);
        sr.record(uuid3, 31.);
        sr.record(uuid3, 32.);
        sr.record(uuid3, 33.);
        assertEquals(sr.getAverage(uuid1), 10.5, DELTA); // average of [10., 11.]
        assertEquals(sr.getAverage(uuid2), 20., DELTA); // average of [20.]
        assertEquals(sr.getAverage(uuid3), 32., DELTA); // average of [31., 32., 33.]
        sr.record(uuid1, 12.);
        sr.record(uuid1, 13.);
        sr.record(uuid2, 21.);
        sr.record(uuid3, 34.);
        assertEquals(sr.getAverage(uuid1), 12., DELTA); // average of [11., 12., 13.]
        assertEquals(sr.getAverage(uuid2), 20.5, DELTA); // average of [20., 21.]
        assertEquals(sr.getAverage(uuid3), 33., DELTA); // average of [32., 33., 34.]
    }

    @Test(
            expectedExceptions = NoSuchElementException.class,
            expectedExceptionsMessageRegExp = "No campaign with the given id found"
    )
    public void testAbsentCampaign() {
        final int N = 3;
        var sr = new SampleRecorderImpl(N);

        final var uuid1 = new UUID(0, 0);
        final var uuid2 = new UUID(0, 1);
        final var uuid3 = new UUID(0, 2);

        sr.record(uuid1, 1.);
        sr.record(uuid2, 2.);
        sr.record(uuid2, 2.);
        sr.getAverage(uuid3);
    }
}