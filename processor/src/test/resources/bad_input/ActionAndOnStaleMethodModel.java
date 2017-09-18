import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnStale;

@Container
public class ActionAndOnStaleMethodModel
{
  @Action
  @OnStale
  public long doStuff()
  {
    return 22;
  }
}
