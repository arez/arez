import arez.annotations.Action;
import arez.annotations.ArezComponent;

public class NestedModel
{
  @ArezComponent
  public abstract static class BasicActionModel
  {
    @Action
    void doStuff( final long time )
    {
    }
  }
}
