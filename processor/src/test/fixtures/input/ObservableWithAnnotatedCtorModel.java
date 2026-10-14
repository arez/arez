import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings( "WeakerAccess" )
@ArezComponent
public abstract class ObservableWithAnnotatedCtorModel
{
  @NullMarked
  @interface DoNotCopyThisAnnotation
  {
  }

  ObservableWithAnnotatedCtorModel( final long time,
                                    @DoNotCopyThisAnnotation final long other,
                                    @NonNull final String foo )
  {
  }

  @Observable
  public long getTime()
  {
    return 0;
  }

  @Observable
  public void setTime( final long time )
  {
  }
}
