package arez.integration;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.component.Identifiable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "ConstantConditions" )
public class IdentifiableIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void arezManagerdArezId()
  {
    final Model1 model = new IdentifiableIntegrationTest_Arez_Model1();

    assertTrue( model instanceof Identifiable );
    assertEquals( Identifiable.getArezId( model ), (Integer) 0 );
    assertEquals( Identifiable.asIdentifiable( model ), model );
  }

  @Test
  public void componentManagerdArezId()
  {
    final Model2 model = new IdentifiableIntegrationTest_Arez_Model2( 33 );

    assertTrue( model instanceof Identifiable );
    assertEquals( Identifiable.getArezId( model ), (Integer) 33 );
    assertEquals( Identifiable.asIdentifiable( model ), model );
  }

  @ArezComponent( allowEmpty = true )
  static abstract class Model1
  {
  }

  @ArezComponent( allowEmpty = true )
  static abstract class Model2
  {
    private final int id;

    Model2( final int id )
    {
      this.id = id;
    }

    @ComponentId
    final int getId()
    {
      return id;
    }
  }
}
