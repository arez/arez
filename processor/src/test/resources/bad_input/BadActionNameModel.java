import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;

@Container
public class BadActionNameModel
{
  @Action( name = "-ace" )
  public void setField()
  {
  }
}
