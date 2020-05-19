package org.mechdancer.filters.signal

import org.mechdancer.algebra.implement.vector.toListVector

/** 采样率为 [fs] 的离散功率信号 */
class PowerSignal(
    val fs: Double,
    private val f: (t: Int) -> Double
) : Signal<Int> {
    override fun get(t: Int) =
        f(t)

    fun delay(t: Int) =
        PowerSignal(fs) { tao: Int -> f(tao - t) }

    fun plus(others: PowerSignal) =
        PowerSignal(fs) { tao: Int -> this[tao] + others[tao] }

    fun minus(others: PowerSignal) =
        PowerSignal(fs) { tao: Int -> this[tao] - others[tao] }

    fun times(others: PowerSignal) =
        PowerSignal(fs) { tao: Int -> this[tao] * others[tao] }

    fun times(k: Double) =
        PowerSignal(fs) { tao: Int -> this[tao] * k }

    fun div(k: Double) =
        PowerSignal(fs) { tao: Int -> this[tao] / k }

    fun truncate(range: IntRange) =
        EnergySignal(fs, range.first, range.map(f).toListVector())
}
