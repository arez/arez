import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class PrivateComputedModel
{
  @Computed
  private long getField()
  {
    return 0;
  }
}
