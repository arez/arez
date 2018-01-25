import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class ObservableModelWithUnconventionalNames
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
