import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerNamePrefix;

@Container
public class ContainerNamePrefixPrivateModel
{
  @Action
  void myAction()
  {
  }

  @ContainerNamePrefix
  private String getTypeName()
  {
    return null;
  }
}
