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

public class MixedInjectWithObservePostConstructTest
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

  static class MyDependency2
  {
    @Inject
    MyDependency2()
    {
      _events.add( "MyDependency2()" );
    }

    @Override
    public String toString()
    {
      return "MyDependency2";
    }
  }

  @ArezComponent( dagger = Feature.ENABLE )
  static abstract class MyComponent
  {
    private final MyDependency2 _dependency2;
    @Inject
    MyDependency _dependency;

    MyComponent( final MyDependency2 dependency2 )
    {
      _dependency2 = dependency2;
      _events.add( "MyComponent()" );
      _events.add( "MyComponent._dependency = " + _dependency );
      _events.add( "MyComponent._dependency2 = " + _dependency2 );
    }

    @PostConstruct
    final void postConstruct()
    {
      _events.add( "MyComponent.postConstruct()" );
      _events.add( "MyComponent._dependency = " + _dependency );
      _events.add( "MyComponent._dependency2 = " + _dependency2 );
    }

    @Observe( depType = DepType.AREZ_OR_NONE )
    void run()
    {
      _events.add( "MyComponent.run()" );
      _events.add( "MyComponent._dependency = " + _dependency );
      _events.add( "MyComponent._dependency2 = " + _dependency2 );
    }
  }

  @Singleton
  @Component( modules = MixedInjectWithObservePostConstructTest_MyComponentDaggerComponentExtension.DaggerModule.class )
  interface TestDaggerComponent
    extends MixedInjectWithObservePostConstructTest_MyComponentDaggerComponentExtension
  {
    MyComponent component1();

    static TestDaggerComponent create()
    {
      return DaggerMixedInjectWithObservePostConstructTest_TestDaggerComponent.create();
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
                  "MyDependency2()\n" +
                  "MyComponent()\n" +
                  "MyComponent._dependency = null\n" +
                  "MyComponent._dependency2 = MyDependency2\n" +
                  "MyDependency()\n" +
                  "MyComponent.postConstruct()\n" +
                  "MyComponent._dependency = MyDependency\n" +
                  "MyComponent._dependency2 = MyDependency2\n" +
                  "MyComponent.run()\n" +
                  "MyComponent._dependency = MyDependency\n" +
                  "MyComponent._dependency2 = MyDependency2\n" +
                  "MyDependency()" );
  }
}
