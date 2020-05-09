package org.mechdancer.filters.signal

import org.mechdancer.filters.algebra.Complex
import kotlin.math.max
import kotlin.math.min

/** 复信号 */
class ComplexSignal(
    val tBegin: Int,
    val values: List<Complex>
) {
    /** 信号长度 */
    val length get() = values.size

    val tEnd get() = tBegin + length

    /** 信号能量 */
    val energy get() = values.sumByDouble(Complex::norm)

    /** 访问信号采样点 */
    operator fun get(t: Int): Complex =
        (t - tBegin)
            .takeIf { it in values.indices }
            ?.let(values::get)
        ?: Complex.zero

    operator fun plus(others: ComplexSignal): ComplexSignal {
        val begin = min(tBegin, others.tBegin)
        val end = max(tEnd, others.tEnd)
        return ComplexSignal(begin, (begin until end).map { this[it] + others[it] })
    }

    operator fun minus(others: ComplexSignal): ComplexSignal {
        val begin = min(tBegin, others.tBegin)
        val end = max(tEnd, others.tEnd)
        return ComplexSignal(begin, (begin until end).map { this[it] - others[it] })
    }

    operator fun times(others: ComplexSignal): ComplexSignal {
        val begin = max(tBegin, others.tBegin)
        val end = min(tEnd, others.tEnd)
        return ComplexSignal(begin, (begin until end).map { this[it] * others[it] })
    }

    operator fun times(k: Double) =
        ComplexSignal(tBegin, values.map { it * Complex(k) })

    operator fun div(k: Double) =
        ComplexSignal(tBegin, values.map { it / Complex(k) })
}
