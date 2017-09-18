import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnActivate;

@Container
public class OnActivateDuplicateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate( name = "myValue" )
  void foo()
  {
  }

  @OnActivate
  void onMyValueActivate()
  {
  }
}
