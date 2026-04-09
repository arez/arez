package arez.persist.runtime;

import arez.SafeProcedure;
import arez.persist.AbstractTest;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

@SuppressWarnings( { "unchecked", "rawtypes" } )
public final class ConverterTest
  extends AbstractTest
{
  @Test
  public void getConverter_noConverterRegistered()
  {
    assertEquals( Registry.getConverters().size(), 0 );

    final Converter<Integer, ?> converter = ArezPersist.getConverter( Integer.class );

    assertEquals( Registry.getConverters().size(), 0 );

    final Integer value = 33;
    assertSame( converter.encode( value ), value );

    assertSame( ( (Converter) converter ).decode( converter.encode( value ) ), value );
  }

  @Test
  public void getConverter_converterRegistered()
  {
    final Converter converter = mock( Converter.class );

    assertEquals( Registry.getConverters().size(), 0 );

    ArezPersist.registerConverter( String.class, converter );

    assertEquals( Registry.getConverters().size(), 1 );

    assertSame( ArezPersist.getConverter( String.class ), converter );
  }

  @Test
  public void registerConverter_duplicate()
  {
    final Converter converter1 = mock( Converter.class );
    final Converter converter2 = mock( Converter.class );

    assertEquals( Registry.getConverters().size(), 0 );

    ArezPersist.registerConverter( String.class, converter1 );

    assertEquals( Registry.getConverters().size(), 1 );

    assertInvariantFailure( () -> ArezPersist.registerConverter( String.class, converter2 ),
                            "registerConverter() invoked with type 'java.lang.String' but a converter is already registered for that type" );
  }

  @Test
  public void deregisterConverter()
  {
    final Converter converter = mock( Converter.class );

    assertEquals( Registry.getConverters().size(), 0 );

    final SafeProcedure deregisterAction = ArezPersist.registerConverter( String.class, converter );

    assertEquals( Registry.getConverters().size(), 1 );

    deregisterAction.call();

    assertEquals( Registry.getConverters().size(), 0 );
  }
}
