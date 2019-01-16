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

public class NonCtorInjectNoSchedulesNoPostConstructTest
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
    @Inject
    MyDependency _dependency;

    MyComponent()
    {
      _events.add( "MyComponent()" );
      _events.add( "MyComponent._dependency = " + _dependency );
    }

    void run()
    {
      _events.add( "MyComponent.run()" );
      _events.add( "MyComponent._dependency = " + _dependency );
    }
  }

  @Singleton
  @Component( modules = NonCtorInjectNoSchedulesNoPostConstructTest_MyComponentDaggerModule.class )
  interface TestDaggerComponent
  {
    MyComponent component1();

    static TestDaggerComponent create()
    {
      return DaggerNonCtorInjectNoSchedulesNoPostConstructTest_TestDaggerComponent.create();
    }
  }

  @Test
  public void scenario()
  {
    final TestDaggerComponent daggerComponent = TestDaggerComponent.create();
    assertEquals( String.join( "\n", _events ), "" );
    final MyComponent component = daggerComponent.component1();
    component.run();
    assertEquals( String.join( "\n", _events ),
                  "MyComponent()\n" +
                  "MyComponent._dependency = null\n" +
                  "MyDependency()\n" +
                  "MyComponent.run()\n" +
                  "MyComponent._dependency = MyDependency" );
  }
}
