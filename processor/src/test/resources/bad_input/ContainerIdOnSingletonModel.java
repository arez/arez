import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerId;
import org.realityforge.arez.annotations.Observable;

@Container( singleton = true )
public class ContainerIdOnSingletonModel
{
  @ContainerId
  final long getId()
  {
    return 0;
  }

  @Observable
  public long getField()
  {
    return 0;
  }

  @Observable
  public void setField( final long field )
  {
  }
}
