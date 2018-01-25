import arez.annotations.Action;
import arez.annotations.ArezComponent;

public class NestedNestedModel
{
  public static class Something
  {
    @ArezComponent
    public static abstract class BasicActionModel
    {
      @Action
      public void doStuff( final long time )
      {
      }
    }
  }
}
