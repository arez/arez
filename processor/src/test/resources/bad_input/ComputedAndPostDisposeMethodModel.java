import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PostDispose;

@Container
public class ComputedAndPostDisposeMethodModel
{
  @Computed
  @PostDispose
  public long getField()
  {
    return 22;
  }
}
