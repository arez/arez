import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PreDispose;

@Container
public class ComputedAndPreDisposeMethodModel
{
  @Computed
  @PreDispose
  public long getField()
  {
    return 22;
  }
}
