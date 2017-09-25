import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerNamePrefix;

@Container
public class ContainerNamePrefixFinalModel
{
  @Action
  void myAction()
  {
  }

  @ContainerNamePrefix
  final String getTypeName()
  {
    return null;
  }
}
