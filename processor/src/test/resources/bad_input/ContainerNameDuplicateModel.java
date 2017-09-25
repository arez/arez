import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerName;

@Container
public class ContainerNameDuplicateModel
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

  @ContainerName
  String getTypeName2()
  {
    return null;
  }
}
