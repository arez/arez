import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDispose;

@Container
public class OnDisposePrivateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDispose
  private void onMyValueDispose()
  {
  }
}
