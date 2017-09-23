import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDispose;

@Container
public class OnDisposeStaticModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDispose
  static void onMyValueDispose()
  {
  }
}
