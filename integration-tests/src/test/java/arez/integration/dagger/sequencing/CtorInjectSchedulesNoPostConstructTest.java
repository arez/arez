package arez.integration.dagger.sequencing;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Feature;
import arez.annotations.Observe;
import arez.integration.AbstractArezIntegrationTest;
import dagger.Component;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class CtorInjectSchedulesNoPostConstructTest
  extends AbstractArezIntegrationTest
{
  @Nonnull
  private static final List<String> _events = new ArrayList<>();

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

  @ArezComponent( dagger = Feature.ENABLE )
  public static abstract class MyComponent
  {
    private final MyDependency _dependency;

    MyComponent( final MyDependency dependency )
    {
      _events.add( "MyComponent()" );
      _events.add( "MyComponent._dependency = " + dependency );
      _dependency = dependency;
    }

    @Observe( depType = DepType.AREZ_OR_NONE )
    void run()
    {
      _events.add( "MyComponent.run()" );
      _events.add( "MyComponent._dependency = " + _dependency );
    }
  }

  @Singleton
  @Component( modules = CtorInjectSchedulesNoPostConstructTest_MyComponentDaggerModule.class )
  interface TestDaggerComponent
  {
    MyComponent component1();

    static TestDaggerComponent create()
    {
      return DaggerCtorInjectSchedulesNoPostConstructTest_TestDaggerComponent.create();
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
                  "MyComponent._dependency = MyDependency\n" +
                  "MyComponent.run()\n" +
                  "MyComponent._dependency = MyDependency" );
  }
}
