import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PostDispose;

@Container
public class PostDisposeDuplicateModel
{
  @PostDispose
  void foo()
  {
  }

  @PostDispose
  void doStuff()
  {
  }
}
