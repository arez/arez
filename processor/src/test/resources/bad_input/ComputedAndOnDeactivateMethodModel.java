import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDeactivate;

@Container
public class ComputedAndOnDeactivateMethodModel
{
  @Computed
  @OnDeactivate
  public long getField()
  {
    return 22;
  }
}
