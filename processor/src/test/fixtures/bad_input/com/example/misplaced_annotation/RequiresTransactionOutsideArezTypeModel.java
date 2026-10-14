package com.example.misplaced_annotation;

import arez.annotations.RequiresTransaction;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class RequiresTransactionOutsideArezTypeModel
{
  @RequiresTransaction
  void perform()
  {
  }
}
