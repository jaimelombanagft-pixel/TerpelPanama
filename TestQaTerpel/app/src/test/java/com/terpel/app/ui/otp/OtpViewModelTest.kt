package com.terpel.app.ui.otp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*

@OptIn(ExperimentalCoroutinesApi::class)
class OtpViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        kotlinx.coroutines.Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        kotlinx.coroutines.Dispatchers.resetMain()
    }

    @Test
    fun timer_starts_at_60() = runTest(dispatcher) {
        val vm = OtpViewModel()
        vm.startTimer()
        Assert.assertEquals(60, vm.remaining.value)
    }

    @Test
    fun verify_succeeds_with_6_digits() = runTest(dispatcher) {
        val vm = OtpViewModel()
        vm.verify("123456")
        val start = System.currentTimeMillis()
        while (vm.verified.value != true && System.currentTimeMillis() - start < 1200) {
            Thread.sleep(50)
        }
        Assert.assertTrue(vm.verified.value == true)
    }
}
