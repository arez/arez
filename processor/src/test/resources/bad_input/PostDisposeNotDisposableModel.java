import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PostDispose;

@Container( disposable = false )
public class PostDisposeNotDisposableModel
{
  @PostDispose
  void doStuff()
  {
  }
}
