package com.example.misplaced_annotation;

import arez.annotations.PostConstruct;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class PostConstructOutsideArezTypeModel
{
  @PostConstruct
  void initialize()
  {
  }
}
