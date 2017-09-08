import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;

@Container
public class DuplicateActionModel
{
  @Action( name = "ace" )
  public void setField()
  {
  }

  @Action( name = "ace" )
  public void setField2()
  {
  }
}
