package com.example.requires_transaction;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.RequiresTransaction;

@ArezComponent
abstract class MemoizeAndRequiresTransactionModel
{
  @Memoize
  @RequiresTransaction
  int perform()
  {
    return 1;
  }
}
