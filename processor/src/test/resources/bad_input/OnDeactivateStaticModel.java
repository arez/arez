import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDeactivate;

@Container
public class OnDeactivateStaticModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate
  static void onMyValueDeactivate()
  {
  }
}
