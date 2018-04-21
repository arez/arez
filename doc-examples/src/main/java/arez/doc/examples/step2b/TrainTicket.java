package arez.doc.examples.step2b;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent
public abstract class TrainTicket
{
  public static TrainTicket create( int remainingRides )
  {
    return new Arez_TrainTicket( remainingRides );
  }

  @Observable( initializer = Feature.ENABLE )
  public abstract int getRemainingRides();

  public abstract void setRemainingRides( int remainingRides );
}
