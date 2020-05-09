package org.mechdancer.filters.signal

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.vector.div
import org.mechdancer.algebra.function.vector.times
import org.mechdancer.algebra.implement.vector.toListVector
import kotlin.math.max
import kotlin.math.min

/** （离散）能量信号 */
class EnergySignal(val tBegin: Int = 0, val values: Vector)
    : Signal<Int> {
    /** 信号长度 */
    val length get() = values.dim

    val tEnd get() = tBegin + length

    /** 信号能量 */
    val energy get() = values.length

    /** 访问信号采样点 */
    override operator fun get(t: Int) =
        (t - tBegin).takeIf { it in 0 until values.dim }?.let(values::get) ?: .0

    fun delay(t: Int) =
        EnergySignal(tBegin + t, values)

    operator fun plus(others: EnergySignal): EnergySignal {
        val begin = min(tBegin, others.tBegin)
        val end = max(tEnd, others.tEnd)
        return EnergySignal(begin, (begin until end).map { this[it] + others[it] }.toListVector())
    }

    operator fun minus(others: EnergySignal): EnergySignal {
        val begin = min(tBegin, others.tBegin)
        val end = max(tEnd, others.tEnd)
        return EnergySignal(begin, (begin until end).map { this[it] - others[it] }.toListVector())
    }

    operator fun times(others: EnergySignal): EnergySignal {
        val begin = max(tBegin, others.tBegin)
        val end = min(tEnd, others.tEnd)
        return EnergySignal(begin, (begin until end).map { this[it] * others[it] }.toListVector())
    }

    operator fun times(k: Double) =
        EnergySignal(tBegin, values * k)

    operator fun div(k: Double) =
        EnergySignal(tBegin, values / k)

    infix fun dot(others: EnergySignal): Double {
        val begin = max(tBegin, others.tBegin)
        val end = min(tEnd, others.tEnd)
        return (begin until end).sumByDouble { this[it] * others[it] }
    }

    infix fun xcorr(others: EnergySignal) =
        EnergySignal(
            tBegin - others.length + 1,
            List(length + others.length - 1) {
                this dot others.delay(1 - others.length + it)
            }.toListVector())
}
