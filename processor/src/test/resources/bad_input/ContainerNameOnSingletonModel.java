import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerName;

@Container( singleton = true )
public class ContainerNameOnSingletonModel
{
  @Action
  void myAction()
  {
  }

  @ContainerName
  String getTypeName()
  {
    return null;
  }
}
