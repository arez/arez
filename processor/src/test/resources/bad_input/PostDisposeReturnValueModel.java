import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PostDispose;
import org.realityforge.arez.annotations.PreDispose;

@Container
public class PostDisposeReturnValueModel
{
  @PostDispose
  int doStuff()
  {
    return 0;
  }
}
