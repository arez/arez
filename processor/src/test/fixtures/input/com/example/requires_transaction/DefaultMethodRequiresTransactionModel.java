package com.example.requires_transaction;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class DefaultMethodRequiresTransactionModel
  implements DefaultRequiresTransactionMethods
{
}
