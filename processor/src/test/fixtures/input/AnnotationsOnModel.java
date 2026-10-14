import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class AnnotationsOnModel
{
  @NullMarked
  public @interface DoNotCopyThisAnnotation
  {
  }

  @Observable
  @NonNull
  @DoNotCopyThisAnnotation
  public String getTime()
  {
    return "";
  }

  @Observable
  @DoNotCopyThisAnnotation
  public void setTime( @NonNull @DoNotCopyThisAnnotation final String time )
  {
  }

  @Action
  @DoNotCopyThisAnnotation
  public void doStuff( @NonNull @DoNotCopyThisAnnotation final String time )
  {
  }

  @Memoize
  @NonNull
  @DoNotCopyThisAnnotation
  public Integer someValue()
  {
    return 0;
  }
}
