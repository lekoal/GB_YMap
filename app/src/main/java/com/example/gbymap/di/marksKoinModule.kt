package com.example.gbymap.di

import com.example.gbymap.data.marks.MarksDatabaseHelperImpl
import com.example.gbymap.domain.marks.MarksDatabaseBuilder
import com.example.gbymap.domain.marks.MarksDatabaseHelper
import com.example.gbymap.ui.mainScreen.MarksViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val marksKoinModule = module {
    single(named("marks_database")) {
        MarksDatabaseBuilder.getInstance(androidContext())
    }
    single<MarksDatabaseHelper>(named("marks_db_helper")) {
        MarksDatabaseHelperImpl(get(named("marks_database")))
    }
    viewModel(named("marks_view_model")) {
        MarksViewModel(get(named("marks_db_helper")))
    }
}