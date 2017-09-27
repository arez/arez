import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@SuppressWarnings( "WeakerAccess" )
@ArezComponent
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
