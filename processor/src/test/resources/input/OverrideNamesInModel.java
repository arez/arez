import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container( name = "MyContainer" )
public class OverrideNamesInModel
{
  @Observable( name = "myField" )
  public long getTime()
  {
    return 0;
  }

  @Observable( name = "myField" )
  public void setTime( final long time )
  {
  }

  @Action( name = "myAction" )
  public void doAction()
  {
  }

  @Computed( name = "myComputed" )
  int compute()
  {
    return 0;
  }
}
