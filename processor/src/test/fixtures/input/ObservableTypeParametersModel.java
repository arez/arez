import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class ObservableTypeParametersModel<T extends Integer>
{
  @Observable
  public @Nullable T getTime()
  {
    return null;
  }

  public void setTime( final @Nullable T time )
  {
  }
}
