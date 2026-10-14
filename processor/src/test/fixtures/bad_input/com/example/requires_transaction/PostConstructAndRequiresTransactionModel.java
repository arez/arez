package com.example.requires_transaction;

import arez.annotations.ArezComponent;
import arez.annotations.PostConstruct;
import arez.annotations.RequiresTransaction;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class PostConstructAndRequiresTransactionModel
{
  @PostConstruct
  @RequiresTransaction
  void initialize()
  {
  }
}
