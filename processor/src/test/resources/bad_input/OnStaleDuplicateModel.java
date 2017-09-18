import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnStale;

@Container
public class OnStaleDuplicateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnStale( name = "myValue" )
  void foo()
  {
  }

  @OnStale
  void onMyValueStale()
  {
  }
}
