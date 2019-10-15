package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.util.SpyEventRecorder;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NestedComputableValueIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final FilterContext filterContext = FilterContext.create();

    /*
      01. People
      02.   - Person
      03.     - Bob
      04.     - Mary
      05.     - Bill
      06.     - Daniel
      07. Vehicle
      08.   - Land Vehicle
      09.     - Passenger Vehicle
      10.       - Sedan 385
      11.       - Slip On 397
      12.       - Sedan 396
      13.     - Heavy Vehicle
      14.       - Dozer
      15.         - Dozer 123
      16.         - Dozer 124
      17.         - Dozer 125
      18.       - Fire Tanker
      19.         - Tanker CFA-2
      20.         - Tanker CFA-4
     */

    final Node node1 = Node.create( filterContext, null, "People" );
    final Node node2 = Node.create( filterContext, node1, "Person" );
    final Node node3 = Node.create( filterContext, node2, "Bob" );
    final Node node4 = Node.create( filterContext, node2, "Mary" );
    final Node node5 = Node.create( filterContext, node2, "Bill" );
    final Node node6 = Node.create( filterContext, node2, "Daniel" );
    final Node node7 = Node.create( filterContext, null, "Vehicle" );
    final Node node8 = Node.create( filterContext, node7, "Land Vehicle" );
    final Node node9 = Node.create( filterContext, node8, "Passenger Vehicle" );
    final Node node10 = Node.create( filterContext, node9, "Sedan 385" );
    final Node node11 = Node.create( filterContext, node9, "Slip On 397" );
    final Node node12 = Node.create( filterContext, node9, "Sedan 396" );
    final Node node13 = Node.create( filterContext, node8, "Heavy Vehicle" );
    final Node node14 = Node.create( filterContext, node13, "Dozer" );
    final Node node15 = Node.create( filterContext, node14, "Dozer 123" );
    final Node node16 = Node.create( filterContext, node14, "Dozer 124" );
    final Node node17 = Node.create( filterContext, node14, "Dozer 125" );
    final Node node18 = Node.create( filterContext, node13, "Fire Tanker" );
    final Node node19 = Node.create( filterContext, node18, "Tanker CFA-2" );
    final Node node20 = Node.create( filterContext, node18, "Tanker CFA-4" );

    context.observer( node1::cacheIsVisibleResult );
    context.observer( node2::cacheIsVisibleResult );
    context.observer( node3::cacheIsVisibleResult );
    context.observer( node4::cacheIsVisibleResult );
    context.observer( node5::cacheIsVisibleResult );
    context.observer( node6::cacheIsVisibleResult );
    context.observer( node7::cacheIsVisibleResult );
    context.observer( node8::cacheIsVisibleResult );
    context.observer( node9::cacheIsVisibleResult );
    context.observer( node10::cacheIsVisibleResult );
    context.observer( node11::cacheIsVisibleResult );
    context.observer( node12::cacheIsVisibleResult );
    context.observer( node13::cacheIsVisibleResult );
    context.observer( node14::cacheIsVisibleResult );
    context.observer( node15::cacheIsVisibleResult );
    context.observer( node16::cacheIsVisibleResult );
    context.observer( node17::cacheIsVisibleResult );
    context.observer( node18::cacheIsVisibleResult );
    context.observer( node19::cacheIsVisibleResult );
    context.observer( node20::cacheIsVisibleResult );

    assertTrue( node1.getIsVisibleResult() );
    assertTrue( node2.getIsVisibleResult() );
    assertTrue( node3.getIsVisibleResult() );
    assertTrue( node4.getIsVisibleResult() );
    assertTrue( node5.getIsVisibleResult() );
    assertTrue( node6.getIsVisibleResult() );
    assertTrue( node7.getIsVisibleResult() );
    assertTrue( node8.getIsVisibleResult() );
    assertTrue( node9.getIsVisibleResult() );
    assertTrue( node10.getIsVisibleResult() );
    assertTrue( node11.getIsVisibleResult() );
    assertTrue( node12.getIsVisibleResult() );
    assertTrue( node13.getIsVisibleResult() );
    assertTrue( node14.getIsVisibleResult() );
    assertTrue( node15.getIsVisibleResult() );
    assertTrue( node16.getIsVisibleResult() );
    assertTrue( node17.getIsVisibleResult() );
    assertTrue( node18.getIsVisibleResult() );
    assertTrue( node19.getIsVisibleResult() );
    assertTrue( node20.getIsVisibleResult() );

    context.action( () -> filterContext.setFilter( "o" ) );

    assertTrue( node1.getIsVisibleResult() );
    assertTrue( node2.getIsVisibleResult() );
    assertTrue( node3.getIsVisibleResult() );
    assertFalse( node4.getIsVisibleResult() );
    assertFalse( node5.getIsVisibleResult() );
    assertFalse( node6.getIsVisibleResult() );
    assertTrue( node7.getIsVisibleResult() );
    assertTrue( node8.getIsVisibleResult() );
    assertFalse( node9.getIsVisibleResult() );
    assertFalse( node10.getIsVisibleResult() );
    assertFalse( node11.getIsVisibleResult() );
    assertFalse( node12.getIsVisibleResult() );
    assertTrue( node13.getIsVisibleResult() );
    assertTrue( node14.getIsVisibleResult() );
    assertTrue( node15.getIsVisibleResult() );
    assertTrue( node16.getIsVisibleResult() );
    assertTrue( node17.getIsVisibleResult() );
    assertFalse( node18.getIsVisibleResult() );
    assertFalse( node19.getIsVisibleResult() );
    assertFalse( node20.getIsVisibleResult() );

    context.action( () -> filterContext.setFilter( "ob" ) );

    assertTrue( node1.getIsVisibleResult() );
    assertTrue( node2.getIsVisibleResult() );
    assertTrue( node3.getIsVisibleResult() );
    assertFalse( node4.getIsVisibleResult() );
    assertFalse( node5.getIsVisibleResult() );
    assertFalse( node6.getIsVisibleResult() );
    assertFalse( node7.getIsVisibleResult() );
    assertFalse( node8.getIsVisibleResult() );
    assertFalse( node9.getIsVisibleResult() );
    assertFalse( node10.getIsVisibleResult() );
    assertFalse( node11.getIsVisibleResult() );
    assertFalse( node12.getIsVisibleResult() );
    assertFalse( node13.getIsVisibleResult() );
    assertFalse( node14.getIsVisibleResult() );
    assertFalse( node15.getIsVisibleResult() );
    assertFalse( node16.getIsVisibleResult() );
    assertFalse( node17.getIsVisibleResult() );
    assertFalse( node18.getIsVisibleResult() );
    assertFalse( node19.getIsVisibleResult() );
    assertFalse( node20.getIsVisibleResult() );

    context.action( () -> filterContext.setFilter( "obb" ) );

    assertFalse( node1.getIsVisibleResult() );
    assertFalse( node2.getIsVisibleResult() );
    assertFalse( node3.getIsVisibleResult() );
    assertFalse( node4.getIsVisibleResult() );
    assertFalse( node5.getIsVisibleResult() );
    assertFalse( node6.getIsVisibleResult() );
    assertFalse( node7.getIsVisibleResult() );
    assertFalse( node8.getIsVisibleResult() );
    assertFalse( node9.getIsVisibleResult() );
    assertFalse( node10.getIsVisibleResult() );
    assertFalse( node11.getIsVisibleResult() );
    assertFalse( node12.getIsVisibleResult() );
    assertFalse( node13.getIsVisibleResult() );
    assertFalse( node14.getIsVisibleResult() );
    assertFalse( node15.getIsVisibleResult() );
    assertFalse( node16.getIsVisibleResult() );
    assertFalse( node17.getIsVisibleResult() );
    assertFalse( node18.getIsVisibleResult() );
    assertFalse( node19.getIsVisibleResult() );
    assertFalse( node20.getIsVisibleResult() );

    context.action( () -> filterContext.setFilter( "ob" ) );

    assertTrue( node1.getIsVisibleResult() );
    assertTrue( node2.getIsVisibleResult() );
    assertTrue( node3.getIsVisibleResult() );
    assertFalse( node4.getIsVisibleResult() );
    assertFalse( node5.getIsVisibleResult() );
    assertFalse( node6.getIsVisibleResult() );
    assertFalse( node7.getIsVisibleResult() );
    assertFalse( node8.getIsVisibleResult() );
    assertFalse( node9.getIsVisibleResult() );
    assertFalse( node10.getIsVisibleResult() );
    assertFalse( node11.getIsVisibleResult() );
    assertFalse( node12.getIsVisibleResult() );
    assertFalse( node13.getIsVisibleResult() );
    assertFalse( node14.getIsVisibleResult() );
    assertFalse( node15.getIsVisibleResult() );
    assertFalse( node16.getIsVisibleResult() );
    assertFalse( node17.getIsVisibleResult() );
    assertFalse( node18.getIsVisibleResult() );
    assertFalse( node19.getIsVisibleResult() );
    assertFalse( node20.getIsVisibleResult() );

    assertMatchesFixture( recorder );
  }

  @Test
  public void scenarioWithActions()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final FilterContext filterContext = FilterContext.create();

    /*
      01. People
      02.   - Person
      03.     - Bob
     */

    final Node node1 = Node.create( filterContext, null, "People" );
    final Node node2 = Node.create( filterContext, node1, "Person" );
    final Node node3 = Node.create( filterContext, node2, "Bob" );

    assertTrue( context.action( node3::isVisible ) );
    assertTrue( context.action( node2::isVisible ) );
    assertTrue( context.action( node1::isVisible ) );

    context.action( () -> filterContext.setFilter( "o" ) );

    assertTrue( context.action( node3::isVisible ) );
    assertTrue( context.action( node2::isVisible ) );
    assertTrue( context.action( node1::isVisible ) );

    context.action( () -> filterContext.setFilter( "ob" ) );

    assertTrue( context.action( node3::isVisible ) );
    assertTrue( context.action( node2::isVisible ) );
    assertTrue( context.action( node1::isVisible ) );

    context.action( () -> filterContext.setFilter( "obb" ) );

    assertFalse( context.action( node3::isVisible ) );
    assertFalse( context.action( node2::isVisible ) );
    assertFalse( context.action( node1::isVisible ) );

    context.action( () -> filterContext.setFilter( "ob" ) );

    assertTrue( context.action( node3::isVisible ) );
    assertTrue( context.action( node2::isVisible ) );
    assertTrue( context.action( node1::isVisible ) );

    assertMatchesFixture( recorder );
  }

  @ArezComponent
  static abstract class FilterContext
  {
    private String _filter = "";

    @Nonnull
    static FilterContext create()
    {
      return new NestedComputableValueIntegrationTest_Arez_FilterContext();
    }

    @Observable
    String getFilter()
    {
      return _filter;
    }

    void setFilter( final String filter )
    {
      _filter = filter;
    }
  }

  @SuppressWarnings( { "WeakerAccess", "Arez:UnmanagedComponentReference" } )
  @ArezComponent
  static abstract class Node
  {
    private final FilterContext _context;
    private final ArrayList<Node> _children = new ArrayList<>();
    private String _name;
    // Cached result used in observed tests
    private boolean _isVisibleResult;

    @Nonnull
    static Node create( @Nonnull final FilterContext context,
                        @Nullable final Node parent,
                        @Nonnull final String name )
    {
      return new NestedComputableValueIntegrationTest_Arez_Node( context, parent, name );
    }

    Node( @Nonnull final FilterContext context, @Nullable final Node parent, @Nonnull final String name )
    {
      _context = context;
      _name = name;
      if ( null != parent )
      {
        parent.getChildren().add( this );
      }
    }

    @Memoize
    boolean isVisible()
    {
      return getChildren().isEmpty() ?
             getName().contains( getContext().getFilter() ) :
             getChildren().stream().anyMatch( Node::isVisible );
    }

    @Observable
    String getName()
    {
      return _name;
    }

    void setName( final String name )
    {
      _name = name;
    }

    FilterContext getContext()
    {
      return _context;
    }

    ArrayList<Node> getChildren()
    {
      return _children;
    }

    boolean getIsVisibleResult()
    {
      return _isVisibleResult;
    }

    void cacheIsVisibleResult()
    {
      observeADependency();
      _isVisibleResult = isVisible();
    }
  }
}
