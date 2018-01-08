import arez.annotations.Action;
import arez.annotations.ArezComponent;

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
