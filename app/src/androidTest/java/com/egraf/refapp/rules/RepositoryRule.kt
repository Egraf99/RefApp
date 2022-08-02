package com.egraf.refapp.rules

import androidx.test.platform.app.InstrumentationRegistry
import com.egraf.refapp.GameRepository
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RepositoryRule: TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                val instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
                GameRepository.initialize(instrumentationContext)
            }
        }
    }
}