import org.mechdancer.algebra.function.vector.normalize
import org.mechdancer.algebra.function.vector.plus
import org.mechdancer.algebra.function.vector.times
import org.mechdancer.algebra.implement.vector.listVectorOfZero
import org.mechdancer.algebra.implement.vector.toListVector
import org.mechdancer.algebra.implement.vector.vector2D
import org.mechdancer.filters.signal.EnergySignal
import org.mechdancer.remote.presets.RemoteHub
import org.mechdancer.remote.presets.remoteHub
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

fun main() {
    val remote = remoteHub("信号仿真").apply {
        openAllNetworks()
        println(networksInfo())
    }

//    val signal = EnergySignal(values = (.0..2 * PI).sample(.1, ::sin))
    val delay = 200

    while (true) {
        val engine = java.util.Random()
        val signal = EnergySignal(values = List(500) { engine.nextGaussian() }.toListVector())

        val environment = List(delay) { i ->
            (1 - i / delay.toDouble()) * Random.nextDouble() / (delay * .25)
        }
        var output = EnergySignal(values = listVectorOfZero(1))
        environment.forEachIndexed { i, k ->
            output += signal.delay(i) * k
        }
        output = output.noise(4.0)
        remote.paint("发射", signal)
        remote.paint("接收", output)
        remote.paint("自相关", (signal xcorr signal) * .01)
        remote.paint("互相关", (output xcorr signal) * .5)
        Thread.sleep(1000)
    }
}

// 采样
private fun ClosedFloatingPointRange<Double>.sample(
    step: Double,
    f: (Double) -> Double
) =
    sequence {
        var t = start
        while (t < endInclusive) {
            yield(f(t))
            t += step
        }
    }.toList().toListVector()

// 加噪
private fun EnergySignal.noise(snr: Double): EnergySignal {
    val p = values.length / snr
    val engine = java.util.Random()
    val n = List(length) { engine.nextGaussian() }.toListVector().normalize() * p
    return EnergySignal(tBegin, values + n)
}

// 画信号
private fun RemoteHub.paint(topic: String, signal: EnergySignal) =
    signal.values.toList()
        .mapIndexed { i, value -> vector2D(signal.tBegin + i, value) }
        .let { paintFrame2(topic, listOf(it)) }
