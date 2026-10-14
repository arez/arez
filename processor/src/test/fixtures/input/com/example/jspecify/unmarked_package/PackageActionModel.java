package com.example.jspecify.unmarked_package;

import arez.annotations.*;

@ArezComponent(sting = Feature.DISABLE)
abstract class PackageActionModel {
  @Action
  String echo(String value) { return value; }
}
