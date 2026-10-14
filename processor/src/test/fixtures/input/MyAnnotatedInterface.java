import arez.annotations.Action;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface MyAnnotatedInterface
{
  @Observable
  default long getTime()
  {
    return 0;
  }

  @Observable
  default void setTime( final long time )
  {
  }

  @Action
  default void doStuff( final long time )
  {
  }

  @Memoize
  default int someValue()
  {
    return 0;
  }
}
