package arez.persist.runtime.browser;

import arez.persist.AbstractTest;
import arez.persist.runtime.ArezPersist;
import arez.persist.runtime.Converter;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ConvertersTest
  extends AbstractTest
{
  @Test
  public void encodeAndDecode()
  {
    ArezPersistBrowserUtil.registerCharacterConverter();
    ArezPersistBrowserUtil.registerByteConverter();
    ArezPersistBrowserUtil.registerShortConverter();
    ArezPersistBrowserUtil.registerIntegerConverter();
    ArezPersistBrowserUtil.registerLongConverter();
    ArezPersistBrowserUtil.registerFloatConverter();
    assertEncodeAndDecode( Character.class, 'a', "a" );
    assertEncodeAndDecode( Byte.class, (byte) 2, 2.0D );
    assertEncodeAndDecode( Short.class, (short) 2, 2.0D );
    assertEncodeAndDecode( Integer.class, 2, 2.0D );
    assertEncodeAndDecode( Long.class, 2L, 2.0D );
    assertEncodeAndDecode( Float.class, 2.0F, 2.0D );
    assertEncodeAndDecode( Double.class, 2.0, 2.0D );
    assertEncodeAndDecode( String.class, "ace", "ace" );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  private <T> void assertEncodeAndDecode( @Nonnull final Class<T> type,
                                          @Nonnull final T value,
                                          @Nonnull final Object encodedValue )
  {
    final Converter converter = ArezPersist.getConverter( type );
    assertEquals( converter.encode( value ), encodedValue );
    assertEquals( converter.decode( encodedValue ), value );
  }
}
