package arez.persist.runtime.browser;

import arez.persist.StoreTypes;
import arez.persist.runtime.ArezPersist;
import javax.annotation.Nonnull;

public final class ArezPersistBrowserUtil
{
  private ArezPersistBrowserUtil()
  {
  }

  /**
   * Register a store under the {@link StoreTypes#SESSION} name that saves state in a browsers session storage.
   *
   * @param persistenceKey the key under which the state is stored.
   */
  public static void registerSessionStore( @Nonnull final String persistenceKey )
  {
    ArezPersist.registerStore( StoreTypes.SESSION, WebStorageService.createSessionStorageService( persistenceKey ) );
  }

  /**
   * Register a store under the {@link StoreTypes#LOCAL} name that saves state in a browsers local storage.
   *
   * @param persistenceKey the key under which the state is stored.
   */
  public static void registerLocalStore( @Nonnull final String persistenceKey )
  {
    ArezPersist.registerStore( StoreTypes.LOCAL, WebStorageService.createLocalStorageService( persistenceKey ) );
  }

  /**
   * Register a character converter that is useful when converting to json representation.
   */
  public static void registerCharacterConverter()
  {
    ArezPersist.registerConverter( Character.class, new CharacterConverter() );
  }

  /**
   * Register a byte converter that is useful when converting to json representation.
   */
  public static void registerByteConverter()
  {
    ArezPersist.registerConverter( Byte.class, new ByteConverter() );
  }

  /**
   * Register a Short converter that is useful when converting to json representation.
   */
  public static void registerShortConverter()
  {
    ArezPersist.registerConverter( Short.class, new ShortConverter() );
  }

  /**
   * Register a Integer converter that is useful when converting to json representation.
   */
  public static void registerIntegerConverter()
  {
    ArezPersist.registerConverter( Integer.class, new IntegerConverter() );
  }

  /**
   * Register a Long converter that is useful when converting to json representation.
   */
  public static void registerLongConverter()
  {
    ArezPersist.registerConverter( Long.class, new LongConverter() );
  }

  /**
   * Register a Float converter that is useful when converting to json representation.
   */
  public static void registerFloatConverter()
  {
    ArezPersist.registerConverter( Float.class, new FloatConverter() );
  }
}
