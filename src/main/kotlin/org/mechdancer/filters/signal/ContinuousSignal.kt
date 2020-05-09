package org.mechdancer.filters.signal

/** 连续信号 */
inline class ContinuousSignal(private val f: (t: Double) -> Double)
    : Signal<Double> {
    override fun get(t: Double) =
        f(t)

    fun delay(t: Double) =
        ContinuousSignal { tao: Double -> f(tao - t) }

    operator fun plus(others: ContinuousSignal) =
        ContinuousSignal { tao: Double -> this[tao] + others[tao] }

    operator fun minus(others: ContinuousSignal) =
        ContinuousSignal { tao: Double -> this[tao] - others[tao] }

    operator fun times(others: ContinuousSignal) =
        ContinuousSignal { tao: Double -> this[tao] * others[tao] }

    operator fun times(k: Double) =
        ContinuousSignal { tao: Double -> this[tao] * k }

    operator fun div(k: Double) =
        ContinuousSignal { tao: Double -> this[tao] / k }

    fun sample(t: Double) =
        PowerSignal { k: Int -> f(k * t) }
}
