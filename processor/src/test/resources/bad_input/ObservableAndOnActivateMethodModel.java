import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.OnActivate;

@Container
public class ObservableAndOnActivateMethodModel
{
  private long _field;

  @Action
  public long getField()
  {
    return _field;
  }

  @Observable
  @OnActivate
  public void setField( final long field )
  {
    _field = field;
  }
}
