package arez.doc.examples.step2;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class TrainTicket
{
  private int _remainingRides;

  public static TrainTicket create( int remainingRides )
  {
    return new Arez_TrainTicket( remainingRides );
  }

  TrainTicket( int remainingRides )
  {
    _remainingRides = remainingRides;
  }

  @Observable
  public int getRemainingRides()
  {
    return _remainingRides;
  }

  public void setRemainingRides( int remainingRides )
  {
    _remainingRides = remainingRides;
  }
}
