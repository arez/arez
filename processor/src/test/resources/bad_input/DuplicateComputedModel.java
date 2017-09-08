import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class DuplicateComputedModel
{
  @Computed( name = "ace" )
  public int getX()
  {
    return 0;
  }

  @Computed( name = "ace" )
  public int getX2()
  {
    return 0;
  }
}
