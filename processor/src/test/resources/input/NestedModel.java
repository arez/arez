import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;

public class NestedModel
{
  @Container
  public static class BasicActionModel
  {
    @Action
    public void doStuff( final long time )
    {
    }
  }
}
