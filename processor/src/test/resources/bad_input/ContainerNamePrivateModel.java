import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerName;

@Container
public class ContainerNamePrivateModel
{
  @Action
  void myAction()
  {
  }

  @ContainerName
  private String getTypeName()
  {
    return null;
  }
}
