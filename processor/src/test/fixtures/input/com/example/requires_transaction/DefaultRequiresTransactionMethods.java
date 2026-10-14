package com.example.requires_transaction;

import arez.annotations.RequiresTransaction;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface DefaultRequiresTransactionMethods
{
  @RequiresTransaction
  default int performDefault()
  {
    return 7;
  }
}
