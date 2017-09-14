import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PostDispose;
import org.realityforge.arez.annotations.PreDispose;

@Container
public class PreDisposeAndPostDisposeMethodModel
{
  @PreDispose
  @PostDispose
  public long doStuff()
  {
    return 22;
  }
}
