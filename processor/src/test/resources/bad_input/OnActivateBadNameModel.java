import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnActivate;

@Container
public class OnActivateBadNameModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate
  void foo()
  {
  }
}
