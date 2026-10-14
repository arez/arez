package arez.doc.examples.step1;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class TrainTicket {
    private int _remainingRides;

    TrainTicket(int remainingRides) {
        _remainingRides = remainingRides;
    }

    @Observable
    public int getRemainingRides() {
        return _remainingRides;
    }

    public void setRemainingRides(int remainingRides) {
        _remainingRides = remainingRides;
    }
}
