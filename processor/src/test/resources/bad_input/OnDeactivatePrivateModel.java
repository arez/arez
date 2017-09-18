import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDeactivate;

@Container
public class OnDeactivatePrivateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate
  private void onMyValueDeactivate()
  {
  }
}
