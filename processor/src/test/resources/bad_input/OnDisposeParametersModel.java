import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDispose;

@Container
public class OnDisposeParametersModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDispose
  void onMyValueDispose( int x )
  {
  }
}
