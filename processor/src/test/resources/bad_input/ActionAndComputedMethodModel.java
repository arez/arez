import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class ActionAndComputedMethodModel
{
  @Computed
  @Action
  public long doStuff()
  {
    return 22;
  }
}
