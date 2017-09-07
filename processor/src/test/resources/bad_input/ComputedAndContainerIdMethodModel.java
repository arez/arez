import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerId;

@Container
public class ComputedAndContainerIdMethodModel
{
  @ContainerId
  @Computed
  public long getField()
  {
    return 22;
  }
}
