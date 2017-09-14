import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PostDispose;
import org.realityforge.arez.annotations.PreDispose;

@Container
public class PostDisposeParametersModel
{
  @PostDispose
  void doStuff( int i )
  {
  }
}
