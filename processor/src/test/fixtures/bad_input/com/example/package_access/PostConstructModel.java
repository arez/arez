package com.example.package_access;

import arez.annotations.ArezComponent;
import com.example.package_access.other.BasePostConstructModel;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class PostConstructModel
  extends BasePostConstructModel
{
}
