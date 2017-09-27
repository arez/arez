import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

public class NestedNestedModel
{
  public static class Something
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
}
