import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerId;

@Container
public class ActionAndContainerIdMethodModel
{
  @ContainerId
  @Action
  public long doStuff()
  {
    return 22;
  }
}
