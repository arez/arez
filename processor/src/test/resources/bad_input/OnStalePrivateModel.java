import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnStale;

@Container
public class OnStalePrivateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnStale
  private void onMyValueStale()
  {
  }
}
