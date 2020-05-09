package org.mechdancer.filters.signal

import org.mechdancer.algebra.implement.vector.toListVector

/** （离散）功率信号 */
inline class PowerSignal(private val f: (t: Int) -> Double)
    : Signal<Int> {
    override fun get(t: Int) =
        f(t)

    fun delay(t: Int) =
        PowerSignal { tao: Int -> f(tao - t) }

    fun plus(others: PowerSignal) =
        PowerSignal { tao: Int -> this[tao] + others[tao] }

    fun minus(others: PowerSignal) =
        PowerSignal { tao: Int -> this[tao] - others[tao] }

    fun times(others: PowerSignal) =
        PowerSignal { tao: Int -> this[tao] * others[tao] }

    fun times(k: Double) =
        PowerSignal { tao: Int -> this[tao] * k }

    fun div(k: Double) =
        PowerSignal { tao: Int -> this[tao] / k }

    fun truncate(range: IntRange) =
        EnergySignal(range.first, range.map(f).toListVector())
}
