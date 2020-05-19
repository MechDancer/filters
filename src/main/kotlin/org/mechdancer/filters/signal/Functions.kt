package org.mechdancer.filters.signal

import org.mechdancer.algebra.function.vector.normalize
import org.mechdancer.algebra.function.vector.plus
import org.mechdancer.algebra.function.vector.times
import org.mechdancer.algebra.implement.vector.toListVector

/** 加信噪比为 [snr] 的高斯白噪声 */
fun EnergySignal.noise(snr: Double): EnergySignal {
    val p = values.length / snr
    val engine = java.util.Random()
    val n = List(length) { engine.nextGaussian() }.toListVector().normalize() * p
    return EnergySignal(fs, tBegin, values + n)
}
