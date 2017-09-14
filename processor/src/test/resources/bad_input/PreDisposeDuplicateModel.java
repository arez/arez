import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PreDispose;

@Container
public class PreDisposeDuplicateModel
{
  @PreDispose
  void foo()
  {
  }

  @PreDispose
  void doStuff()
  {
  }
}
