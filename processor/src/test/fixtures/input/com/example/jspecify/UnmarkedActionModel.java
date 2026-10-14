package com.example.jspecify;

import arez.ArezContext;
import arez.annotations.*;
import org.jspecify.annotations.*;

@NullUnmarked
@ArezComponent(sting = Feature.DISABLE)
abstract class UnmarkedActionModel extends UnmarkedActionBase {
  @Action
  String classScope(String value) { return value; }

  @NullMarked
  @Action
  String marked(String value) { return value; }

  static class NestedBase {
    @Action
    String methodScope(String value) { return value; }
  }

  @NullMarked
  static class Owner<T> {
    class Inner<U> {}
    class Plain {}
  }

  @NullMarked
  @ArezComponent(sting = Feature.DISABLE)
  abstract static class Nested<T extends @NonNull Object> extends NestedBase {
    @NullUnmarked
    @ContextRef
    abstract @NonNull ArezContext context();

    @Observable abstract T getItem();
    abstract void setItem(T item);

    @Observable abstract java.util.List<?> getAny();
    abstract void setAny(java.util.List<?> any);

    @Observable abstract Owner<String>.Inner<String> getInner();
    abstract void setInner(Owner<String>.Inner<String> inner);

    @Observable abstract Owner<String>.Plain getPlain();
    abstract void setPlain(Owner<String>.Plain plain);

    @NullUnmarked
    Nested(String incoming) {}

    @Observable
    abstract java.util.List<String @Nullable []> getValues();
    abstract void setValues(java.util.List<String @Nullable []> values);

    @NullUnmarked
    @Action
    @Nullable String explicit(@NonNull String value) { return value; }
  }
}
