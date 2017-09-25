import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerNamePrefix;

@Container
public class ContainerNamePrefixStaticModel
{
  @Action
  void myAction()
  {
  }

  @ContainerNamePrefix
  static String getTypeName()
  {
    return null;
  }
}
