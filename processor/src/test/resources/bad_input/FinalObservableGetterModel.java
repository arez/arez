import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class FinalObservableGetterModel
{
  private long _field;

  @Observable
  public final long getField()
  {
    return _field;
  }

  @Observable
  public void setField( final long field )
  {
    _field = field;
  }
}
