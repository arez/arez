import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerNamePrefix;

@Container
public class ContainerNamePrefixDuplicateModel
{
  @Action
  void myAction()
  {
  }

  @ContainerNamePrefix
  String getTypeName()
  {
    return null;
  }

  @ContainerNamePrefix
  String getTypeName2()
  {
    return null;
  }
}
