import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public interface InterfaceModel
{
  @Observable
  default long getField()
  {
    return 1;
  }

  @Observable
  default void setField( final long field )
  {
  }
}
