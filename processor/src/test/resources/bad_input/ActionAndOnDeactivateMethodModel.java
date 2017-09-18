import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDeactivate;

@Container
public class ActionAndOnDeactivateMethodModel
{
  @Action
  @OnDeactivate
  public long doStuff()
  {
    return 22;
  }
}
