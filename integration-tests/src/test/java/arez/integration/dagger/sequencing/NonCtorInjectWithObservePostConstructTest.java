package arez.integration.dagger.sequencing;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Feature;
import arez.annotations.Observe;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NonCtorInjectWithObservePostConstructTest
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

  @ArezComponent( dagger = Feature.ENABLE )
  static abstract class MyComponent
  {
    @Inject
    MyDependency _dependency;

    MyComponent()
    {
      _events.add( "MyComponent()" );
      _events.add( "MyComponent._dependency = " + _dependency );
    }

    @PostConstruct
    final void postConstruct()
    {
      _events.add( "MyComponent.postConstruct()" );
      _events.add( "MyComponent._dependency = " + _dependency );
    }

    @Observe( depType = DepType.AREZ_OR_NONE )
    void run()
    {
      _events.add( "MyComponent.run()" );
      _events.add( "MyComponent._dependency = " + _dependency );
    }
  }

  @Singleton
  @Component( modules = NonCtorInjectWithObservePostConstructTest_MyComponentDaggerComponentExtension.DaggerModule.class )
  interface TestDaggerComponent
    extends NonCtorInjectWithObservePostConstructTest_MyComponentDaggerComponentExtension
  {
    MyComponent component1();

    static TestDaggerComponent create()
    {
      return DaggerNonCtorInjectWithObservePostConstructTest_TestDaggerComponent.create();
    }
  }

  @Test
  public void scenario()
  {
    final TestDaggerComponent daggerComponent = TestDaggerComponent.create();
    daggerComponent.bindMyComponent();
    assertEquals( String.join( "\n", _events ), "" );
    daggerComponent.component1();
    assertEquals( String.join( "\n", _events ),
                  "MyComponent()\n" +
                  "MyComponent._dependency = null\n" +
                  "MyDependency()\n" +
                  "MyComponent.postConstruct()\n" +
                  "MyComponent._dependency = MyDependency\n" +
                  "MyComponent.run()\n" +
                  "MyComponent._dependency = MyDependency\n" +
                  "MyDependency()" );
  }
}
