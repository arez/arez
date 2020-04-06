package arez.integration.observable;

import arez.annotations.ArezComponent;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class UnannotatedObservablesIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final Model model = Model.create();
    final List<String> trace = new ArrayList<>();
    safeAction( () -> model.setValue( "A" ) );
    observer( () -> trace.add( model.getValue() ) );

    assertEquals( String.join( ",", trace ), "A" );

    safeAction( () -> model.setValue( "B" ) );

    assertEquals( String.join( ",", trace ), "A,B" );

    safeAction( () -> {
      model.setValue( "C" );
      model.setValue( "D" );
    } );

    assertEquals( String.join( ",", trace ), "A,B,D" );
  }

  @ArezComponent
  static abstract class Model
  {
    @Nonnull
    static Model create()
    {
      return new UnannotatedObservablesIntegrationTest_Arez_Model();
    }

    abstract String getValue();

    abstract void setValue( String lastName );
  }
}
