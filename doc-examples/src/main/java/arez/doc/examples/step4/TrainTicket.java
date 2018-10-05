package arez.doc.examples.step4;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
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

  @Action
  public void rideTrain()
  {
    setRemainingRides( getRemainingRides() - 1 );
  }

  @Observe
  void notifyUserWhenTicketExpires()
  {
    if ( 0 == getRemainingRides() )
    {
      NotifyTool.notifyUserTicketExpired( this );
    }
  }
}
