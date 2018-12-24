package arez.integration.dagger.sequencing;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.integration.AbstractArezIntegrationTest;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class CtorInjectNoSchedulesNoPostConstructTest
  extends AbstractArezIntegrationTest
{
  private static final ArrayList<String> _events = new ArrayList<>();

  static class MyDependency
  {
    @Inject
    MyDependency()
    {
      _events.add( "MyDependency()" );
    }

    @Override
    public String toString()
    {
      return "MyDependency";
    }
  }

  @ArezComponent( dagger = Feature.ENABLE, allowEmpty = true )
  public static abstract class MyComponent
  {
    MyComponent( final MyDependency dependency )
    {
      _events.add( "MyComponent()" );
      _events.add( "MyComponent._dependency = " + dependency );
    }
  }

  @Singleton
  @Component( modules = CtorInjectNoSchedulesNoPostConstructTest_MyComponentDaggerModule.class )
  interface TestDaggerComponent
  {
    MyComponent component1();

    static TestDaggerComponent create()
    {
      return DaggerCtorInjectNoSchedulesNoPostConstructTest_TestDaggerComponent.create();
    }
  }

  @Test
  public void scenario()
  {
    final TestDaggerComponent daggerComponent = TestDaggerComponent.create();
    assertEquals( String.join( "\n", _events ), "" );
    daggerComponent.component1();
    assertEquals( String.join( "\n", _events ),
                  "MyDependency()\n" +
                  "MyComponent()\n" +
                  "MyComponent._dependency = MyDependency" );
  }
}
