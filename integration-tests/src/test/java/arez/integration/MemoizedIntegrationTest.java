package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Component;
import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MemoizedIntegrationTest
  extends AbstractIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger[] searchCounts = new AtomicInteger[]
      {
        new AtomicInteger(),
        new AtomicInteger(),
        new AtomicInteger(),
        new AtomicInteger()
      };

    final SpyEventRecorder recorder = new SpyEventRecorder();
    context.getSpy().addSpyEventHandler( recorder );

    final PersonModel person = PersonModel.create( "Bill", 15 );

    context.autorun( "SearchResult - red",
                     () -> {
                       if ( Disposable.isNotDisposed( person ) )
                       {
                         recorder.mark( "doesSearchMatch - red", person.doesSearchMatch( "red" ) );
                       }
                       searchCounts[ 0 ].incrementAndGet();
                     } );
    final Observer observer3 =
      context.autorun( "SearchResult - ill",
                       () -> {
                         recorder.mark( "doesSearchMatch - ill", person.doesSearchMatch( "ill" ) );
                         searchCounts[ 1 ].incrementAndGet();
                       } );
    context.autorun( "SearchResult - red - 20",
                     () -> {
                       if ( Disposable.isNotDisposed( person ) )
                       {
                         recorder.mark( "doesSearchMatch - red", person.doesFullSearchMatch( "red", 20 ) );
                       }
                       searchCounts[ 2 ].incrementAndGet();
                     } );
    context.autorun( "SearchResult - red - 5",
                     () -> {
                       if ( Disposable.isNotDisposed( person ) )
                       {
                         recorder.mark( "doesSearchMatch - red", person.doesFullSearchMatch( "red", 5 ) );
                       }
                       searchCounts[ 3 ].incrementAndGet();
                     } );

    for ( final AtomicInteger count : searchCounts )
    {
      count.set( 0 );
    }

    context.action( "Update 0", true, () -> person.setName( "Gill" ) );
    context.action( "Update 1", true, () -> person.setName( "Fred" ) );
    context.action( "Update 2", true, () -> person.setName( "Donald" ) );

    observer3.dispose();

    context.action( "Update 3", true, () -> person.setName( "Fred" ) );
    context.action( "Update 3", true, () -> person.setName( "Bill" ) );

    assertEquals( searchCounts[ 0 ].get(), 4 );
    assertEquals( searchCounts[ 1 ].get(), 1 );
    assertEquals( searchCounts[ 2 ].get(), 0 );
    assertEquals( searchCounts[ 3 ].get(), 4 );

    Disposable.dispose( person );

    assertEqualsFixture( recorder.eventsAsString() );
    assertEquals( searchCounts[ 0 ].get(), 5 );
    assertEquals( searchCounts[ 1 ].get(), 1 );
    assertEquals( searchCounts[ 2 ].get(), 1 );
    assertEquals( searchCounts[ 3 ].get(), 5 );
  }

  @Test
  public void scenario_actionOnly()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = new SpyEventRecorder();
    context.getSpy().addSpyEventHandler( recorder );

    final PersonModel person = PersonModel.create( "Bill", 15 );

    context.action( "Query 1", true, () -> person.doesSearchMatch( "ill" ) );
    context.action( "Query 2", true, () -> person.doesSearchMatch( "red" ) );

    assertEqualsFixture( recorder.eventsAsString() );
  }

  @Test
  public void scenario_disposeWithoutDeactivate()
    throws Throwable
  {
    setIgnoreObserverErrors( true );
    setPrintObserverErrors( false );

    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = new SpyEventRecorder();
    context.getSpy().addSpyEventHandler( recorder );

    final PersonModel person = PersonModel.create( "Bill", 15 );

    context.autorun( "SearchResult - red - 20",
                     () -> recorder.mark( "doesSearchMatch - red", person.doesFullSearchMatch( "red", 20 ) ) );

    Disposable.dispose( person );

    assertEqualsFixture( recorder.eventsAsString() );
  }

  @Test
  public void scenario_disposeNoNativeComponentsEnabled()
    throws Throwable
  {
    ArezTestUtil.disableNativeComponents();
    setIgnoreObserverErrors( true );
    setPrintObserverErrors( false );

    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = new SpyEventRecorder();
    context.getSpy().addSpyEventHandler( recorder );

    final PersonModel person = PersonModel.create( "Bill", 15 );

    context.autorun( "SearchResult - red - 20",
                     () -> recorder.mark( "doesSearchMatch - red", person.doesFullSearchMatch( "red", 20 ) ) );

    Disposable.dispose( person );

    assertEqualsFixture( recorder.eventsAsString() );
  }

  @SuppressWarnings( "WeakerAccess" )
  @ArezComponent
  public static abstract class PersonModel
  {
    private String _name;
    private int _age;

    @Nonnull
    public static PersonModel create( @Nonnull final String name, final int age )
    {
      return new MemoizedIntegrationTest_Arez_PersonModel( name, age );
    }

    PersonModel( @Nonnull final String name, final int age )
    {
      _name = name;
      _age = age;
    }

    @ComponentRef
    abstract Component getComponent();

    @Observable
    public String getName()
    {
      return _name;
    }

    public void setName( @Nonnull final String name )
    {
      _name = name;
    }

    @Observable
    public int getAge()
    {
      return _age;
    }

    public void setAge( final int age )
    {
      _age = age;
    }

    @Memoize
    public boolean doesSearchMatch( @Nonnull final String value )
    {
      return getName().contains( value );
    }

    @Memoize
    public boolean doesFullSearchMatch( @Nonnull final String value, final int minAge )
    {
      return getName().contains( value ) && getAge() >= minAge;
    }
  }
}
