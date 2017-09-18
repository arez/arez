import javax.annotation.PostConstruct;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class PostConstructNotPrivateModel
{
  @PostConstruct
  private void postConstruct()
  {
  }

  @Computed
  public int someValue()
  {
    return 0;
  }
}
