import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings( "WeakerAccess" )
@ArezComponent
public abstract class ObservableWithCtorModel
{
  private long _time;

  ObservableWithCtorModel( final long time )
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
