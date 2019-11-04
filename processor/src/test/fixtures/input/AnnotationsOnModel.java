import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class AnnotationsOnModel
{
  public @interface DoNotCopyThisAnnotation
  {
  }

  @Observable
  @Nonnull
  @DoNotCopyThisAnnotation
  public String getTime()
  {
    return "";
  }

  @Observable
  @DoNotCopyThisAnnotation
  public void setTime( @Nonnull @DoNotCopyThisAnnotation final String time )
  {
  }

  @Action
  @DoNotCopyThisAnnotation
  public void doStuff( @Nonnull @DoNotCopyThisAnnotation final String time )
  {
  }

  @Memoize
  @Nonnull
  @DoNotCopyThisAnnotation
  public Integer someValue()
  {
    return 0;
  }
}
