package arez.persist.runtime;

import arez.persist.AbstractTest;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class TypeConverterTest
  extends AbstractTest
{
  @SuppressWarnings( "rawtypes" )
  @Test
  public void basicOperation()
  {
    // key1 has no converter
    final String key1 = ValueUtil.randomString();

    // key2 has a converter
    final String key2 = ValueUtil.randomString();

    final Map<String, Converter> converters = new HashMap<>();
    converters.put( key2, new CharacterConverter() );
    final TypeConverter converter = new TypeConverter( converters );

    assertEquals( converter.encode( key1, null ), (Object) null );
    assertEquals( converter.encode( key1, "NoConvert" ), "NoConvert" );
    assertEquals( converter.decode( key1, null ), (Object) null );
    assertEquals( converter.decode( key1, "NoConvert" ), "NoConvert" );
    assertEquals( converter.encode( key2, null ), (Object) null );
    assertEquals( converter.encode( key2, 'A' ), "A" );
    assertEquals( converter.decode( key2, null ), (Character) null );
    assertEquals( converter.decode( key2, "A" ), (Character) 'A' );
  }

  private static final class CharacterConverter
    implements Converter<Character, String>
  {
    @Override
    public Character decode( @Nonnull final String encoded )
    {
      return encoded.charAt( 0 );
    }

    @Override
    public String encode( @Nonnull final Character value )
    {
      return value.toString();
    }
  }
}
