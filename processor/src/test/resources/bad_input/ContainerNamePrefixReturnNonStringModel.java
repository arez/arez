import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerNamePrefix;

@Container
public class ContainerNamePrefixReturnNonStringModel
{
  @Action
  void myAction()
  {
  }

  @ContainerNamePrefix
  Integer getTypeName()
  {
    return null;
  }
}
