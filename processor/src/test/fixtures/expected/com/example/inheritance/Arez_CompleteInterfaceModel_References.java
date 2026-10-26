package com.example.inheritance;

import com.example.inheritance.other.OtherElement;
import javax.annotation.processing.Generated;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Internal bridge for generated relationship code. Not an application API.
 */
@NullMarked
@Generated("arez.processor.ArezProcessor")
public final class Arez_CompleteInterfaceModel_References {
  private Arez_CompleteInterfaceModel_References() {
  }

  /**
   * Forwards an internal relationship operation.
   */
  public static void $$arezir$$_parentGeneralisation_zset(
      final CompleteInterfaceModel $$arezv$$_component, final @Nullable OtherElement otherElement) {
    ( (Arez_CompleteInterfaceModel) $$arezv$$_component ).$$arezir$$_parentGeneralisation_zset( otherElement );
  }

  /**
   * Forwards an internal relationship operation.
   */
  public static void $$arezir$$_parentGeneralisation_zunset(
      final CompleteInterfaceModel $$arezv$$_component, final OtherElement otherElement) {
    ( (Arez_CompleteInterfaceModel) $$arezv$$_component ).$$arezir$$_parentGeneralisation_zunset( otherElement );
  }

  /**
   * Forwards an internal relationship operation.
   */
  public static void $$arezir$$_otherElements_add(final CompleteInterfaceModel $$arezv$$_component,
      final OtherElement otherElement) {
    ( (Arez_CompleteInterfaceModel) $$arezv$$_component ).$$arezir$$_otherElements_add( otherElement );
  }

  /**
   * Forwards an internal relationship operation.
   */
  public static void $$arezir$$_otherElements_remove(
      final CompleteInterfaceModel $$arezv$$_component, final OtherElement otherElement) {
    ( (Arez_CompleteInterfaceModel) $$arezv$$_component ).$$arezir$$_otherElements_remove( otherElement );
  }
}
