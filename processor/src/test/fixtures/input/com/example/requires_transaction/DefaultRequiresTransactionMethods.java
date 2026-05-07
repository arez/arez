package com.example.requires_transaction;

import arez.annotations.RequiresTransaction;

interface DefaultRequiresTransactionMethods
{
  @RequiresTransaction
  default int performDefault()
  {
    return 7;
  }
}
