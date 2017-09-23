import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDispose;

@Container
public class OnDisposeReturnValueModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDispose
  int onMyValueDispose()
  {
    return 0;
  }
}
