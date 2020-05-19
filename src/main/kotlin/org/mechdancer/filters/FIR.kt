package org.mechdancer.filters

import org.mechdancer.algebra.implement.vector.toListVector
import org.mechdancer.filters.algebra.Complex
import org.mechdancer.filters.algebra.Complex.Companion
import org.mechdancer.filters.algebra.asRe
import org.mechdancer.filters.algebra.sum
import org.mechdancer.filters.signal.ContinuousSignal
import org.mechdancer.filters.signal.EnergySignal
import kotlin.math.PI
import kotlin.math.sin

class FIR(k: List<Double>) {
    private val k = k.reversed().map { it.asRe() }

    private val memory =
        MutableList(k.size) { Complex.zero }

    operator fun invoke(value: Complex): Complex {
        memory.removeAt(0)
        memory.add(value)
        return memory.zip(k) { a, b -> a * b }.sum()
    }

    operator fun invoke(signal: EnergySignal): EnergySignal {
        clear()
        return EnergySignal(signal.fs, values = signal.values.toList().map(::invoke))
    }

    fun clear() {
        memory.fill(Complex.zero)
    }

    override fun toString() =
        k.reversed()
            .mapIndexed { i, k -> "$k z^-$i" }
            .joinToString(" + ")
}
