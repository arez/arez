import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class BasicObservableModel
{
  private long _time;

  @Observable
  public long getTime()
  {
    return _time;
  }

  @Observable
  public void setTime( final long time )
  {
    _time = time;
  }
}
