import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class BasicModelWithDifferentAccessLevels
{
  private @Nullable String _value;
  private long _time;

  BasicModelWithDifferentAccessLevels()
  {
  }

  @SuppressWarnings( "Arez:PublicConstructor" )
  public BasicModelWithDifferentAccessLevels( final @Nullable String value, final long time )
  {
    _value = value;
    _time = time;
  }

  @Observable
  long getTime()
  {
    return _time;
  }

  @Observable
  public void setTime( final long time )
  {
    _time = time;
  }

  @Observable
  @Nullable String getValue()
  {
    return _value;
  }

  @Observable
  public void setValue( final @Nullable String value )
  {
    _value = value;
  }

  @Action
  public void doAction()
  {
  }

  @SuppressWarnings( "Arez:ProtectedMethod" )
  @Action
  protected void doAction2()
  {
  }

  @Action
  void doAction3()
  {
  }
}
