import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnStale;

@Container
public class OnStaleNoComputedModel
{
  @OnStale
  void onMyValueStale()
  {
  }
}
