package com.example.inheritance;

import com.example.inheritance.other.Element;
import javax.annotation.processing.Generated;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Internal bridge for generated relationship code. Not an application API.
 */
@NullMarked
@Generated("arez.processor.ArezProcessor")
public final class Arez_CompleteModel_References {
  private Arez_CompleteModel_References() {
  }

  /**
   * Forwards an internal relationship operation.
   */
  public static void $$arezir$$_elements_add(final CompleteModel $$arezv$$_component,
      final Element element) {
    ( (Arez_CompleteModel) $$arezv$$_component ).$$arezir$$_elements_add( element );
  }

  /**
   * Forwards an internal relationship operation.
   */
  public static void $$arezir$$_elements_remove(final CompleteModel $$arezv$$_component,
      final Element element) {
    ( (Arez_CompleteModel) $$arezv$$_component ).$$arezir$$_elements_remove( element );
  }

  /**
   * Forwards an internal relationship operation.
   */
  public static void $$arezir$$_parentGeneralisation_zset(final CompleteModel $$arezv$$_component,
      final @Nullable Element element) {
    ( (Arez_CompleteModel) $$arezv$$_component ).$$arezir$$_parentGeneralisation_zset( element );
  }

  /**
   * Forwards an internal relationship operation.
   */
  public static void $$arezir$$_parentGeneralisation_zunset(final CompleteModel $$arezv$$_component,
      final Element element) {
    ( (Arez_CompleteModel) $$arezv$$_component ).$$arezir$$_parentGeneralisation_zunset( element );
  }
}
