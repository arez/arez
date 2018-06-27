package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Observable;
import arez.integration.util.SpyEventRecorder;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class NestedComputedValueIntegrationTest
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

    context.autorun( false, node1::cacheIsVisibleResult );
    context.autorun( false, node2::cacheIsVisibleResult );
    context.autorun( false, node3::cacheIsVisibleResult );
    context.autorun( false, node4::cacheIsVisibleResult );
    context.autorun( false, node5::cacheIsVisibleResult );
    context.autorun( false, node6::cacheIsVisibleResult );
    context.autorun( false, node7::cacheIsVisibleResult );
    context.autorun( false, node8::cacheIsVisibleResult );
    context.autorun( false, node9::cacheIsVisibleResult );
    context.autorun( false, node10::cacheIsVisibleResult );
    context.autorun( false, node11::cacheIsVisibleResult );
    context.autorun( false, node12::cacheIsVisibleResult );
    context.autorun( false, node13::cacheIsVisibleResult );
    context.autorun( false, node14::cacheIsVisibleResult );
    context.autorun( false, node15::cacheIsVisibleResult );
    context.autorun( false, node16::cacheIsVisibleResult );
    context.autorun( false, node17::cacheIsVisibleResult );
    context.autorun( false, node18::cacheIsVisibleResult );
    context.autorun( false, node19::cacheIsVisibleResult );
    context.autorun( false, node20::cacheIsVisibleResult );

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

    assertTrue( context.action( false, node3::isVisible ) );
    assertTrue( context.action( false, node2::isVisible ) );
    assertTrue( context.action( false, node1::isVisible ) );

    context.action( () -> filterContext.setFilter( "o" ) );

    assertTrue( context.action( false, node3::isVisible ) );
    assertTrue( context.action( false, node2::isVisible ) );
    assertTrue( context.action( false, node1::isVisible ) );

    context.action( () -> filterContext.setFilter( "ob" ) );

    assertTrue( context.action( false, node3::isVisible ) );
    assertTrue( context.action( false, node2::isVisible ) );
    assertTrue( context.action( false, node1::isVisible ) );

    context.action( () -> filterContext.setFilter( "obb" ) );

    assertFalse( context.action( false, node3::isVisible ) );
    assertFalse( context.action( false, node2::isVisible ) );
    assertFalse( context.action( false, node1::isVisible ) );

    context.action( () -> filterContext.setFilter( "ob" ) );

    assertTrue( context.action( false, node3::isVisible ) );
    assertTrue( context.action( false, node2::isVisible ) );
    assertTrue( context.action( false, node1::isVisible ) );

    assertMatchesFixture( recorder );
  }

  @SuppressWarnings( "WeakerAccess" )
  @ArezComponent
  public static abstract class FilterContext
  {
    private String _filter = "";

    @Nonnull
    public static FilterContext create()
    {
      return new NestedComputedValueIntegrationTest_Arez_FilterContext();
    }

    @Observable
    public String getFilter()
    {
      return _filter;
    }

    public void setFilter( final String filter )
    {
      _filter = filter;
    }
  }

  @SuppressWarnings( "WeakerAccess" )
  @ArezComponent
  public static abstract class Node
  {
    private final FilterContext _context;
    private final Node _parent;
    private final ArrayList<Node> _children = new ArrayList<>();
    private String _name;
    // Cached result used in autorun tests
    private boolean _isVisibleResult;

    @Nonnull
    public static Node create( @Nonnull final FilterContext context,
                               @Nullable final Node parent,
                               @Nonnull final String name )
    {
      return new NestedComputedValueIntegrationTest_Arez_Node( context, parent, name );
    }

    Node( @Nonnull final FilterContext context, @Nullable final Node parent, @Nonnull final String name )
    {
      _context = context;
      _parent = parent;
      _name = name;
      if ( null != parent )
      {
        parent.getChildren().add( this );
      }
    }

    @Computed
    public boolean isVisible()
    {
      return getChildren().isEmpty() ?
             getName().contains( getContext().getFilter() ) :
             getChildren().stream().anyMatch( Node::isVisible );
    }

    @Observable
    public String getName()
    {
      return _name;
    }

    public void setName( final String name )
    {
      _name = name;
    }

    public FilterContext getContext()
    {
      return _context;
    }

    public Node getParent()
    {
      return _parent;
    }

    public ArrayList<Node> getChildren()
    {
      return _children;
    }

    public boolean getIsVisibleResult()
    {
      return _isVisibleResult;
    }

    public void setIsVisibleResult( final boolean isVisibleResult )
    {
      _isVisibleResult = isVisibleResult;
    }

    public void cacheIsVisibleResult()
    {
      observeADependency();
      _isVisibleResult = isVisible();
    }
  }
}
