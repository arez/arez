import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

@ArezComponent( disposable = false )
public class NotDisposableModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }
}
