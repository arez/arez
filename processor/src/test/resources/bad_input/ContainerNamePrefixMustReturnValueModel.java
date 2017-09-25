import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerNamePrefix;

@Container
public class ContainerNamePrefixMustReturnValueModel
{
  @Action
  void myAction()
  {
  }

  @ContainerNamePrefix
  void getTypeName()
  {
  }
}
