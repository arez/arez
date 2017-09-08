import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class ObservableModelWithUnconventionalNames
{
  @Observable
  public long time()
  {
    return 0;
  }

  @Observable
  public void time( final long time )
  {
  }
}
