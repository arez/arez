import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostDispose;
import arez.annotations.PreDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class DisposingModel
{
  @PreDispose
  void preDispose()
  {
  }

  @PostDispose
  void postDispose()
  {
  }

  @Memoize
  public int someValue()
  {
    return 0;
  }
}
