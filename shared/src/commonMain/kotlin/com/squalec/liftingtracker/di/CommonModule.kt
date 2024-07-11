package com.squalec.liftingtracker.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.squalec.liftingtracker.Greeting

val commonModule = module{
    singleOf(::Greeting)
}