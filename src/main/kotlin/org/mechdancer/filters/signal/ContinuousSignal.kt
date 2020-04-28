package org.mechdancer.filters.signal

/** 连续信号 */
inline class ContinuousSignal(private val f: (t: Double) -> Double)
    : Signal<Double> {
    override fun get(t: Double) =
        f(t)

    fun delay(t: Double) =
        ContinuousSignal { tao: Double -> f(tao - t) }

    fun plus(others: ContinuousSignal) =
        ContinuousSignal { tao: Double -> this[tao] + others[tao] }

    fun minus(others: ContinuousSignal) =
        ContinuousSignal { tao: Double -> this[tao] - others[tao] }

    fun times(others: ContinuousSignal) =
        ContinuousSignal { tao: Double -> this[tao] * others[tao] }

    fun times(k: Double) =
        ContinuousSignal { tao: Double -> this[tao] * k }

    fun div(k: Double) =
        ContinuousSignal { tao: Double -> this[tao] / k }
}
