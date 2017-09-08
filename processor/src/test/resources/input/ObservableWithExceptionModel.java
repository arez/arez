import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
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
