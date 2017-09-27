import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

public class NestedModel
{
  @ArezComponent
  public static class BasicActionModel
  {
    @Action
    public void doStuff( final long time )
    {
    }
  }
}
