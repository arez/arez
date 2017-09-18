import javax.annotation.PostConstruct;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class PostConstructMustNotReturnValueModel
{
  @PostConstruct
  int postConstruct()
  {
    return 0;
  }

  @Computed
  public int someValue()
  {
    return 0;
  }
}
