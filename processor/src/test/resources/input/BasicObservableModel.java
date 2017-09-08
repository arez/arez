import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class BasicObservableModel
{
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
