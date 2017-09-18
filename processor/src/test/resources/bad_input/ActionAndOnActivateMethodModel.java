import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnActivate;

@Container
public class ActionAndOnActivateMethodModel
{
  @Action
  @OnActivate
  public long doStuff()
  {
    return 22;
  }
}
