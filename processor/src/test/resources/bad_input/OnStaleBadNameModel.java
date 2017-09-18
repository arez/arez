import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnStale;

@Container
public class OnStaleBadNameModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnStale
  void foo()
  {
  }
}
