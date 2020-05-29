package arez.integration;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;
import arez.component.Identifiable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "ConstantConditions" )
public final class IdentifiableIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void requireId_DISABLE()
  {
    final Model1 model = new IdentifiableIntegrationTest_Arez_Model1();

    assertFalse( model instanceof Identifiable );
  }

  @Test
  public void requireId_ENABLE()
  {
    final Model2 model = new IdentifiableIntegrationTest_Arez_Model2( 33 );

    assertTrue( model instanceof Identifiable );
    assertEquals( Identifiable.getArezId( model ), (Integer) 33 );
    assertEquals( Identifiable.asIdentifiable( model ), model );
  }

  @Test
  public void requireId_DEFAULT()
  {
    final Model3 model = new IdentifiableIntegrationTest_Arez_Model3();

    assertTrue( model instanceof Identifiable );
    assertEquals( Identifiable.getArezId( model ), (Integer) 1 );
    assertEquals( Identifiable.asIdentifiable( model ), model );
  }

  @ArezComponent( allowEmpty = true, requireId = Feature.DISABLE )
  static abstract class Model1
  {
  }

  @ArezComponent( allowEmpty = true, requireId = Feature.ENABLE )
  static abstract class Model2
  {
    private final int id;

    Model2( final int id )
    {
      this.id = id;
    }

    @ComponentId
    int getId()
    {
      return id;
    }
  }

  @ArezComponent( allowEmpty = true, requireId = Feature.AUTODETECT )
  static abstract class Model3
  {
  }
}
