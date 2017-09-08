import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;

@Container
public class BasicFunctionActionModel
{
  @Action
  public int doStuff( final long time )
  {
    return 0;
  }
}
