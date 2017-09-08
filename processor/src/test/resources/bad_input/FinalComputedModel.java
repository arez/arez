import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class FinalComputedModel
{
  @Computed
  public final long getField()
  {
    return 0;
  }
}
