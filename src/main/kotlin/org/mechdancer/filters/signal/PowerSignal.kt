package org.mechdancer.filters.signal

import org.mechdancer.filters.algebra.Complex
import org.mechdancer.filters.algebra.asRe

/** 采样率为 [fs] 的离散功率信号 */
class PowerSignal(
    val fs: Double,
    private val f: (t: Int) -> Complex
) {
    operator fun get(t: Int) = f(t)

    fun delay(t: Int) =
        PowerSignal(fs) { tao: Int -> f(tao - t) }

    fun plus(others: PowerSignal) =
        PowerSignal(fs) { tao: Int -> this[tao] + others[tao] }

    fun minus(others: PowerSignal) =
        PowerSignal(fs) { tao: Int -> this[tao] - others[tao] }

    fun times(others: PowerSignal) =
        PowerSignal(fs) { tao: Int -> this[tao] * others[tao] }

    fun times(k: Double) =
        PowerSignal(fs) { tao: Int -> this[tao] * k.asRe() }

    fun div(k: Double) =
        PowerSignal(fs) { tao: Int -> this[tao] / k.asRe() }

    fun truncate(range: IntRange) =
        EnergySignal(fs, range.first, range.map(f))
}
