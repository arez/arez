import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnActivate;

@Container
public class OnActivateReturnValueModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate
  int onMyValueActivate()
  {
    return 0;
  }
}
