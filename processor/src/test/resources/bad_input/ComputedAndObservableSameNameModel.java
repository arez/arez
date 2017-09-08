import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class ComputedAndObservableSameNameModel
{
  @Computed( name = "x" )
  public long m1()
  {
    return 22;
  }

  @Observable( name = "x" )
  public long getTime()
  {
    return 0;
  }

  @Observable( name = "x" )
  public void setTime( final long time )
  {
  }
}
