import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PreDispose;

@Container
public class PreDisposeStaticModel
{
  @PreDispose
  static void doStuff()
  {
  }
}
