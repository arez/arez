import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class BadComputedName2Model
{
  @Computed( name = "ace-" )
  public int setField()
  {
    return 0;
  }
}
