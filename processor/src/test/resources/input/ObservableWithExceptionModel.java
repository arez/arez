import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
public class ObservableWithExceptionModel
{
  @Observable
  public long getTime()
    throws Exception
  {
    return 0;
  }

  @Observable
  public void setTime( final long time )
    throws Exception
  {
  }
}
