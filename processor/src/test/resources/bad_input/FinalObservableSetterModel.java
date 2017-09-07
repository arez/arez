import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class FinalObservableSetterModel
{
  private long _field;

  @Observable
  public long getField()
  {
    return _field;
  }

  @Observable
  public final void setField( final long field )
  {
    _field = field;
  }
}
