package arez.component;

import arez.AbstractArezTest;
import arez.ArezTestUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class VerifiableTest
  extends AbstractArezTest
{
  static class TestElement
    implements Verifiable
  {
    boolean _verified;

    @Override
    public void verify()
    {
      _verified = true;
    }
  }

  @Test
  public void verifiableElement()
    throws Exception
  {
    final TestElement element = new TestElement();
    assertFalse( element._verified );
    Verifiable.verify( element );
    assertTrue( element._verified );
  }

  @Test
  public void verifiableElement_verifyDisabled()
    throws Exception
  {
    ArezTestUtil.disableVerify();
    final TestElement element = new TestElement();
    assertFalse( element._verified );
    Verifiable.verify( element );
    assertFalse( element._verified );
  }

  @Test
  public void nonLinkableElement()
    throws Exception
  {
    // Should produce no exceptions
    Verifiable.verify( new Object() );
  }
}
