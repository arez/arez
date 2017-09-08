import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class ObservableGuessingModel
{
  @Observable
  public long getTime()
  {
    return 0;
  }

  public void setTime( final long time )
  {
  }

  @Observable
  public void setFoo( boolean x )
  {
  }

  public boolean isFoo()
  {
    return true;
  }

  @Observable
  public void setString( String v )
  {
  }

  public String getString()
  {
    return "";
  }

  public String getString( @Nonnull String someParam )
  {
    return "";
  }

  public void isString()
  {
  }

  //Neither setter not getter has @Observable so should be ignored
  public void setXString( String v )
  {
  }

  public String getXString()
  {
    return "";
  }
}
