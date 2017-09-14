import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PostDispose;
import org.realityforge.arez.annotations.PreDispose;

@Container
public class PostDisposeStaticModel
{
  @PostDispose
  static void doStuff()
  {
  }
}
