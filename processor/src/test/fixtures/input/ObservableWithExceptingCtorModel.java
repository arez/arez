import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings( "WeakerAccess" )
@ArezComponent
public abstract class ObservableWithExceptingCtorModel
{
  @SuppressWarnings( "RedundantThrows" )
  ObservableWithExceptingCtorModel()
    throws Exception
  {
  }

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
