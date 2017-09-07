import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class ExtraParameterGetterModel
{
  private long _field;

  @Observable
  public long getField( final int ignored )
  {
    return _field;
  }

  @Observable
  public void setField( final long field )
  {
    _field = field;
  }
}
