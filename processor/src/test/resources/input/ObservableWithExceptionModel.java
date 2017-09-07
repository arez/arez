import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class ObservableWithExceptionModel
{
  private long _time;

  @Observable
  public long getTime()
    throws Exception
  {
    return _time;
  }

  @Observable
  public void setTime( final long time )
    throws Exception
  {
    _time = time;
  }
}
