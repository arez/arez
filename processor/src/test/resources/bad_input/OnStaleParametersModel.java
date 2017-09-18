import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnStale;

@Container
public class OnStaleParametersModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnStale
  void onMyValueStale( int x )
  {
  }
}
