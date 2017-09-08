import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerId;
import org.realityforge.arez.annotations.Observable;

@Container
public class ContainerIdOnModel
{
  @ContainerId
  public final int getId()
  {
    return 0;
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
