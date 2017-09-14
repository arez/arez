import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PreDispose;

@Container
public class PreDisposeParametersModel
{
  @PreDispose
  void doStuff( int i )
  {
  }
}
