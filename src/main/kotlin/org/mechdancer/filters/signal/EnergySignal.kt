package org.mechdancer.filters.signal

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.vector.div
import org.mechdancer.algebra.function.vector.times
import org.mechdancer.algebra.implement.vector.toListVector
import org.mechdancer.filters.algebra.Complex
import org.mechdancer.filters.algebra.Complex.Companion
import org.mechdancer.filters.algebra.asRe
import org.mechdancer.filters.algebra.sumByComplex
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/** （离散）能量信号 */
class EnergySignal(
    val fs: Double,
    val tBegin: Int = 0,
    val values: List<Complex>
) {
    /** 信号长度 */
    val length get() = values.size

    val tEnd get() = tBegin + length

    /** 信号能量 */
    val energy get() = values.sumByDouble { it.norm }.let(::sqrt)

    /** 访问信号采样点 */
    operator fun get(t: Int) =
        (t - tBegin).takeIf { it in values.indices }?.let(values::get) ?: Complex.zero

    fun delay(t: Int) =
        EnergySignal(fs, tBegin + t, values)

    operator fun plus(others: EnergySignal): EnergySignal {
        val begin = min(tBegin, others.tBegin)
        val end = max(tEnd, others.tEnd)
        return EnergySignal(fs, begin, (begin until end).map { this[it] + others[it] })
    }

    operator fun minus(others: EnergySignal): EnergySignal {
        val begin = min(tBegin, others.tBegin)
        val end = max(tEnd, others.tEnd)
        return EnergySignal(fs, begin, (begin until end).map { this[it] - others[it] })
    }

    operator fun times(others: EnergySignal): EnergySignal {
        val begin = max(tBegin, others.tBegin)
        val end = min(tEnd, others.tEnd)
        return EnergySignal(fs, begin, (begin until end).map { this[it] * others[it] })
    }

    operator fun times(k: Double) =
        EnergySignal(fs, tBegin, values.map(k.asRe()::times))

    operator fun div(k: Double) =
        EnergySignal(fs, tBegin, values.map((1 / k).asRe()::times))

    infix fun dot(others: EnergySignal): Complex {
        val begin = max(tBegin, others.tBegin)
        val end = min(tEnd, others.tEnd)
        return (begin until end).sumByComplex { this[it] * others[it] }
    }

    infix fun xcorr(others: EnergySignal) =
        EnergySignal(fs, tBegin - others.length + 1,
                     List(length + others.length - 1) {
                         this dot others.delay(1 - others.length + it)
                     })
}
