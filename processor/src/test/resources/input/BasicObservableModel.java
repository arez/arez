import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class BasicObservableModel
{
  @Observable
  public long getTime()
  {
    return 0;
  }

  @Observable
  public void setTime( final long time )
  {
  }
}
