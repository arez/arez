package arez.doc.examples.step5;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.Computed;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

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

  @Track
  public String render()
  {
    return "<table class='ticket'>" +
           "  <tr>" +
           "    <th>Remaining Rides</th>" +
           "    <td>" + getRemainingRides() + "</td>" +
           "  </tr>" +
           "</table>";
  }

  @OnDepsChanged
  void onRenderDepsChanged()
  {
    Renderer.scheduleRender( this );
  }

  @Computed
  public boolean isTicketExpired()
  {
    return 0 == getRemainingRides();
  }

  @Autorun
  void notifyUserWhenTicketExpires()
  {
    if ( isTicketExpired() )
    {
      NotifyTool.notifyUserTicketExpired( this );
    }
  }
}
