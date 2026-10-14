package com.example.jspecify.package_scope;
import arez.annotations.*;
@ArezComponent(sting = Feature.DISABLE)
abstract class PackageModel {
  @Observable abstract String getValue();
  abstract void setValue(String value);
}
