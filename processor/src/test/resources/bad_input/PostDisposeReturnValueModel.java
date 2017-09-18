import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PostDispose;

@Container
public class PostDisposeReturnValueModel
{
  @PostDispose
  int doStuff()
  {
    return 0;
  }
}
