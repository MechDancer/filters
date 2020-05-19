package org.mechdancer.filters.signal

import org.mechdancer.algebra.function.vector.normalize
import org.mechdancer.algebra.function.vector.times
import org.mechdancer.algebra.implement.vector.toListVector
import org.mechdancer.filters.algebra.asRe

/** 加信噪比为 [snr] 的高斯白噪声 */
fun EnergySignal.noise(snr: Double): EnergySignal {
    val engine = java.util.Random()
    val n = List(length) { engine.nextGaussian() }.toListVector().normalize() * (energy / snr)
    return EnergySignal(fs, tBegin, values.mapIndexed { i, r -> r + n[i].asRe() })
}
