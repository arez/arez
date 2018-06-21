package arez.integration.dispose;

import arez.ArezTestUtil;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DisposedNotEqualIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    /*
     * To get equal ids we have to disable a few features and then set requireEquals=ENABLED on component.
     * In the real world this matters as it is more often the case that the entity disappears and then reappears
     * and non-disposed variant should not match disposed variant.
     *
     * This happens in replicant where the same graph is unloaded and then reloaded (i.e. navigate away then back).
     */
    ArezTestUtil.disableNativeComponents();
    ArezTestUtil.disableRegistries();
    ArezTestUtil.resetState();

    final String id = ValueUtil.randomString();
    final Model1 model1 = Model1.create( id );
    final Model1 model2 = Model1.create( id );
    final Model1 model3 = Model1.create( id );

    Disposable.dispose( model3 );

    assertEquals( model1.equals( model2 ), true );
    assertEquals( model1.equals( model3 ), false );
    assertEquals( model2.equals( model1 ), true );
    assertEquals( model2.equals( model3 ), false );
    assertEquals( model3.equals( model1 ), false );
    assertEquals( model3.equals( model2 ), false );

    Disposable.dispose( model2 );

    assertEquals( model1.equals( model2 ), false );
    assertEquals( model1.equals( model3 ), false );
    assertEquals( model2.equals( model1 ), false );
    assertEquals( model2.equals( model3 ), true );
    assertEquals( model3.equals( model1 ), false );
    assertEquals( model3.equals( model2 ), true );
  }


  @ArezComponent( allowEmpty = true, requireEquals = Feature.ENABLE )
  static abstract class Model1
  {
    @Nonnull
    static Model1 create( @Nonnull final String id )
    {
      return new DisposedNotEqualIntegrationTest_Arez_Model1( id );
    }

    private final String _id;

    Model1( final String id )
    {
      _id = id;
    }

    @ComponentId
    final String getId()
    {
      return _id;
    }
  }
}
