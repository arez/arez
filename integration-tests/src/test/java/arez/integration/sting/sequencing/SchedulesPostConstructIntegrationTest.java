package arez.integration.sting.sequencing;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Feature;
import arez.annotations.Observe;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import sting.Injectable;
import sting.Injector;
import static org.testng.Assert.*;

public final class SchedulesPostConstructIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Nonnull
  private static final List<String> _events = new ArrayList<>();

  @Injectable
  static class MyDependency
  {
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

  @ArezComponent( sting = Feature.ENABLE )
  public static abstract class MyComponent
  {
    private final MyDependency _dependency;

    MyComponent( final MyDependency dependency )
    {
      _events.add( "MyComponent()" );
      _events.add( "MyComponent._dependency = " + dependency );
      _dependency = dependency;
    }

    @PostConstruct
    void postConstruct()
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

  @Injector( includes = MyComponent.class )
  interface MyInjector
  {
    MyComponent component1();

    static MyInjector create()
    {
      return new SchedulesPostConstructIntegrationTest_Sting_MyInjector();
    }
  }

  @Test
  public void scenario()
  {
    final MyInjector injector = MyInjector.create();
    assertEquals( String.join( "\n", _events ), "" );
    injector.component1();
    assertEquals( String.join( "\n", _events ),
                  "MyDependency()\n" +
                  "MyComponent()\n" +
                  "MyComponent._dependency = MyDependency\n" +
                  "MyComponent.postConstruct()\n" +
                  "MyComponent._dependency = MyDependency\n" +
                  "MyComponent.run()\n" +
                  "MyComponent._dependency = MyDependency" );
  }
}
