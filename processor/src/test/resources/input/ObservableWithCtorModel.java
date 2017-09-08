import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@SuppressWarnings( "WeakerAccess" )
@Container
public class ObservableWithCtorModel
{
  private long _time;

  public ObservableWithCtorModel( final long time )
  {
    _time = time;
  }

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
