import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class NestedModel
{
  @NullMarked
  @ArezComponent
  public abstract static class BasicActionModel
  {
    @Action
    void doStuff( final long time )
    {
    }
  }
}
