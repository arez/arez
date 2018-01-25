import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@SuppressWarnings( "WeakerAccess" )
@ArezComponent
public abstract class ObservableWithExceptingCtorModel
{
  public ObservableWithExceptingCtorModel()
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
