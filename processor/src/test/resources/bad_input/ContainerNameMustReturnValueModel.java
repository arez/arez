import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerName;

@Container
public class ContainerNameMustReturnValueModel
{
  @Action
  void myAction()
  {
  }

  @ContainerName
  void getTypeName()
  {
  }
}
