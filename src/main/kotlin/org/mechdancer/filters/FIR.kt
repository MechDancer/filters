package org.mechdancer.filters

import org.mechdancer.algebra.implement.vector.toListVector
import org.mechdancer.filters.signal.ContinuousSignal
import org.mechdancer.filters.signal.EnergySignal
import kotlin.math.PI
import kotlin.math.sin

class FIR(k: List<Double>) {
    private val k = k.reversed()

    private val memory =
        MutableList(k.size) { .0 }

    operator fun invoke(value: Double): Double {
        memory.removeAt(0)
        memory.add(value)
        return memory.zip(k) { a, b -> a * b }.sum()
    }

    operator fun invoke(signal: EnergySignal): EnergySignal {
        clear()
        return EnergySignal(values = signal.values.toList().map(::invoke).toListVector())
    }

    fun clear() {
        memory.fill(.0)
    }

    override fun toString() =
        k.reversed()
            .mapIndexed { i, k -> "$k z^-$i" }
            .joinToString(" + ")
}
