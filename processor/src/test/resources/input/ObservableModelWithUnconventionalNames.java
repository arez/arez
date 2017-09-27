import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
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
