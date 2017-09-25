import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerName;

@Container
public class ContainerNameStaticModel
{
  @Action
  void myAction()
  {
  }

  @ContainerName
  static String getTypeName()
  {
    return null;
  }
}
