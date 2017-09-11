import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container( disposable = false )
public class NotDisposableModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }
}
