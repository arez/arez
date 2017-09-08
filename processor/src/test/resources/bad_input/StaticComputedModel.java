import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class StaticComputedModel
{
  @Computed
  public static long getField()
  {
    return 0;
  }
}
