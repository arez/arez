import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class ObservableGuessingModel
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
  public void setString( @Nullable String v )
  {
  }

  public @Nullable String getString()
  {
    return "";
  }

  public @Nullable String getString( @NonNull String someParam )
  {
    return "";
  }

  public void isString()
  {
  }

  //Neither setter not getter has @ObservableValue so should be ignored
  public void setXString( @Nullable String v )
  {
  }

  public @Nullable String getXString()
  {
    return "";
  }
}
