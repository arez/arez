import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class ActionAndObservableSameNameModel
{
  @Action( name = "x" )
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
