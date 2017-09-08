import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class MissingObservableSetterModel
{
  private long _field;

  @Observable
  public long getField()
  {
    return _field;
  }
}
