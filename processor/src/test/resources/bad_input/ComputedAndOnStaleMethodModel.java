import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnStale;

@Container
public class ComputedAndOnStaleMethodModel
{
  @Computed
  @OnStale
  public long getField()
  {
    return 22;
  }
}
