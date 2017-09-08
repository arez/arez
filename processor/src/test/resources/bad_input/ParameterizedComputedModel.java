import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class ParameterizedComputedModel
{
  @Computed
  public long getField( final int param )
  {
    return 0;
  }
}
