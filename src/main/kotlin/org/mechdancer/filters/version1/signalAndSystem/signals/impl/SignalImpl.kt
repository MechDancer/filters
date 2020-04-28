package org.mechdancer.filters.version1.signalAndSystem.signals.impl

import org.mechdancer.filters.version1.signalAndSystem.signals.Signal

open class SignalImpl(f: (Int) -> Double) : Signal(f) {
	override infix fun lead(n: Int): Signal = SignalImpl { f(it + n) }
	override fun rise(y: Double): Signal = SignalImpl { f(it) + y }
	override fun times(k: Double): Signal = SignalImpl { f(it) * k }
	override fun reverseInX(x0: Int): Signal = SignalImpl { f(-(it - x0) + x0) }
	override fun reverseInY(y0: Double): Signal = SignalImpl { -(f(it) - y0) + y0 }

	override operator fun plus(other: Signal): Signal = SignalImpl { this[it] + other[it] }
	override operator fun minus(other: Signal): Signal = SignalImpl { this[it] - other[it] }
	override fun x(other: Signal) =
			when (other) {
				is SignalImpl   -> SignalImpl { this[it] * other[it] }
				is EnergySignal -> EnergySignal(other.range) { this[it] * other[it] }
				else            -> throw IllegalStateException()
			}

	override operator fun times(other: Signal) =
			when (other) {
				is SignalImpl   -> throw RuntimeException("无法计算两个无限信号的卷积！")
				is EnergySignal -> SignalImpl { other.range.sumByDouble { k -> this[it - k] * other[k] } }
				else            -> throw IllegalStateException()
			}
}
