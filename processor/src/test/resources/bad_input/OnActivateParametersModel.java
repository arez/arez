import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnActivate;

@Container
public class OnActivateParametersModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate
  void onMyValueActivate( int x )
  {
  }
}
