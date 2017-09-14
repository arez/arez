import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PreDispose;

@Container
public class ActionAndPreDisposeMethodModel
{
  @Action
  @PreDispose
  public long doStuff()
  {
    return 22;
  }
}
