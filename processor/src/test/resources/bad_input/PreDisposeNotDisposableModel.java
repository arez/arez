import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PreDispose;

@Container( disposable = false )
public class PreDisposeNotDisposableModel
{
  @PreDispose
  void doStuff()
  {
  }
}
