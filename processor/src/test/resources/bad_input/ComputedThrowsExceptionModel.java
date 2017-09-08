import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class ComputedThrowsExceptionModel
{
  @Computed
  public long getField()
    throws Exception
  {
    return 0;
  }
}
