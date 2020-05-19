package org.mechdancer.filters.signal

import org.mechdancer.filters.algebra.Complex
import org.mechdancer.filters.algebra.asRe

/** 连续信号 */
inline class ContinuousSignal(private val f: (t: Double) -> Complex) {
    operator fun get(t: Double) = f(t)

    fun delay(t: Double) =
        ContinuousSignal { tao: Double -> f(tao - t) }

    operator fun plus(others: ContinuousSignal) =
        ContinuousSignal { tao: Double -> this[tao] + others[tao] }

    operator fun minus(others: ContinuousSignal) =
        ContinuousSignal { tao: Double -> this[tao] - others[tao] }

    operator fun times(others: ContinuousSignal) =
        ContinuousSignal { tao: Double -> this[tao] * others[tao] }

    operator fun times(k: Double) =
        ContinuousSignal { tao: Double -> this[tao] * k.asRe() }

    operator fun div(k: Double) =
        ContinuousSignal { tao: Double -> this[tao] / k.asRe() }

    fun sample(t: Double) =
        PowerSignal(1 / t) { k: Int -> f(k * t) }
}
