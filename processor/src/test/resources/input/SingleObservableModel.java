import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

/**
 * Example has a single Observable annotation on getter. Setter should be derived.
 */
@Container
public class SingleObservableModel
{
  private long _time;

  @Observable
  public long getTime()
  {
    return _time;
  }

  public void setTime( final long time )
  {
    _time = time;
  }
}
