import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;

public class NestedNestedModel
{
  public static class Something
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
}
