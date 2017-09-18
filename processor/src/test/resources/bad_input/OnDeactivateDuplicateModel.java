import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDeactivate;

@Container
public class OnDeactivateDuplicateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate( name = "myValue" )
  void foo()
  {
  }

  @OnDeactivate
  void onMyValueDeactivate()
  {
  }
}
