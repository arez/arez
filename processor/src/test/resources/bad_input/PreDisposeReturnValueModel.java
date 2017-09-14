import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PreDispose;

@Container
public class PreDisposeReturnValueModel
{
  @PreDispose
  int doStuff()
  {
    return 0;
  }
}
