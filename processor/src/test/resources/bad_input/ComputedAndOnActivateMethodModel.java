import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnActivate;

@Container
public class ComputedAndOnActivateMethodModel
{
  @Computed
  @OnActivate
  public long getField()
  {
    return 22;
  }
}
