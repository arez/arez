package com.example.misplaced_annotation;

import arez.annotations.RequiresTransaction;

abstract class RequiresTransactionOutsideArezTypeModel
{
  @RequiresTransaction
  void perform()
  {
  }
}
