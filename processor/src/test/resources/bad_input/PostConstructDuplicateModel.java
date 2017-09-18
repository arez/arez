import javax.annotation.PostConstruct;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class PostConstructDuplicateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @PostConstruct
  void postConstruct1()
  {
  }

  @PostConstruct
  void postConstruct2()
  {
  }
}
