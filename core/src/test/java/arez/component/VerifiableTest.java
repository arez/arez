package arez.component;

import arez.AbstractTest;
import arez.ArezTestUtil;
import java.io.IOException;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class VerifiableTest
  extends AbstractTest
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

  static class TestBadElement
    implements Verifiable
  {
    boolean _verified;

    @Override
    public void verify()
      throws Exception
    {
      _verified = true;
      throw new IOException();
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

  @Test
  public void verifiableBadElement()
  {
    final TestBadElement element = new TestBadElement();
    assertFalse( element._verified );
    assertThrows( IOException.class, () -> Verifiable.verify( element ) );
    assertTrue( element._verified );
  }
}
