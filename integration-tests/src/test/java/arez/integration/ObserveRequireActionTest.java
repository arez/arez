package arez.integration;

import arez.Arez;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "ResultOfMethodCallIgnored" )
public class ObserveRequireActionTest
  extends AbstractIntegrationTest
{
  @Test
  public void accessingObservableOutsideTransactionShouldThrowException()
  {
    final DisposeIntegrationTest.CodeModel
      component = DisposeIntegrationTest.CodeModel.create( ValueUtil.randomString(), ValueUtil.randomString() );
    assertThrows( component::getName );

    Arez.context().safeAction( component::getName );
  }

  @Test
  public void mutatingObservableOutsideTransactionShouldThrowException()
  {
    final DisposeIntegrationTest.CodeModel
      component = DisposeIntegrationTest.CodeModel.create( ValueUtil.randomString(), ValueUtil.randomString() );
    assertThrows( () -> component.setName( "X" ) );

    Arez.context().safeAction( () -> component.setName( "X" ) );
  }

  @Test
  public void accessingComputedOutsideTransactionShouldThrowException()
  {
    final DisposeIntegrationTest.CodeModel
      component = DisposeIntegrationTest.CodeModel.create( ValueUtil.randomString(), ValueUtil.randomString() );
    assertThrows( component::getQualifiedName );

    Arez.context().safeAction( component::getQualifiedName );
  }
}
