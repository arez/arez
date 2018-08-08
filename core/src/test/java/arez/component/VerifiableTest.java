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
      throws Exception
    {
      _verified = true;
    }
  }

  @Test
  public void verifiableElement()
    throws Exception
  {
    final TestElement element = new TestElement();
    assertEquals( element._verified, false );
    Verifiable.verify( element );
    assertEquals( element._verified, true );
  }

  @Test
  public void verifiableElement_verifyDisabled()
    throws Exception
  {
    ArezTestUtil.disableVerify();
    final TestElement element = new TestElement();
    assertEquals( element._verified, false );
    Verifiable.verify( element );
    assertEquals( element._verified, false );
  }

  @Test
  public void nonLinkableElement()
    throws Exception
  {
    // Should produce no exceptions
    Verifiable.verify( new Object() );
  }
}
