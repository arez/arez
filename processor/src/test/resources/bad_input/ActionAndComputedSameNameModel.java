import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class ActionAndComputedSameNameModel
{
  @Computed( name = "x" )
  public long m1()
  {
    return 22;
  }

  @Action( name = "x" )
  public long m2()
  {
    return 22;
  }
}
