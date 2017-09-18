import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PostDispose;

@Container
public class PostDisposeParametersModel
{
  @PostDispose
  void doStuff( int i )
  {
  }
}
