import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

public final class NonStaticNestedModel
{
  @Container
  public class NestedModel
  {
    private long _field;

    @Observable
    public long getField()
    {
      return _field;
    }

    @Observable
    public void setField( final long field )
    {
      _field = field;
    }
  }
}
