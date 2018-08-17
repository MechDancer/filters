package org.mechdancer.filters.signalAndSystem.signals.impl

import org.mechdancer.filters.signalAndSystem.signals.Signal
import java.io.FileWriter

class EnergySignal(val range: IntRange, f: (Int) -> Double) :
		Signal({ if (it in range) f(it) else .0 }) {
	/**随机信号构造器*/
	constructor(list: List<Double>, origin: Int = 0) :
			this(origin until (origin + list.size), { list[it - origin] })

	val origin get() = range.first
	val end get() = range.last
	val length get() = range.last - range.first + 1

	override infix fun lead(n: Int) = EnergySignal((origin - n)..(end - n)) { f(it + n) }
	override fun rise(y: Double) = EnergySignal(range) { f(it) + y }
	override fun times(k: Double) = EnergySignal(range) { f(it) * k }
	override fun reverseInX(x0: Int) = EnergySignal((2 * x0 - origin)..(2 * x0 - end)) { f(-(it - x0) + x0) }
	override fun reverseInY(y0: Double) = EnergySignal(range) { -(f(it) - y0) + y0 }

	override fun plus(other: Signal) =
			when (other) {
				is SignalImpl   -> SignalImpl { this[it] + other[it] }
				is EnergySignal ->
					EnergySignal(Math.min(this.origin, other.origin)..Math.max(this.end, other.end)) { this[it] + other[it] }
				else            -> throw IllegalStateException()

			}

	override fun minus(other: Signal) =
			when (other) {
				is SignalImpl   -> SignalImpl { this[it] - other[it] }
				is EnergySignal ->
					EnergySignal(Math.min(this.origin, other.origin)..Math.max(this.end, other.end)) { this[it] - other[it] }
				else            -> throw IllegalStateException()

			}

	override fun x(other: Signal) =
			when (other) {
				is SignalImpl   -> EnergySignal(this.range) { this[it] * other[it] }
				is EnergySignal ->
					Math.min(this.end, other.end).let { i ->
						if (Math.max(this.origin, other.origin) >= i) SignalImpl { .0 }
						else EnergySignal(0..i) { k -> this[k] * other[k] }
					}
				else            -> throw IllegalStateException()
			}

	override operator fun times(other: Signal) =
			when (other) {
				is SignalImpl   -> SignalImpl { this.range.sumByDouble { k -> this[k] * other[it - k] } }
				is EnergySignal ->
					EnergySignal((this.origin + other.origin)..(this.end + other.end))
					{ this.range.sumByDouble { k -> this[k] * other[it - k] } }
				else            -> throw IllegalStateException()
			}

	/**变为值列表*/
	fun asList() = DoubleArray(length) { this[it] }.asList()

	/**周期延拓*/
	fun periodicExtension() = PeriodicSignal(this)

	override fun toString() = asList().toString()

	/**导出到文件*/
	infix fun exportTo(fileName: String) {
		var writer = FileWriter(fileName, false)
		writer.write("")
		writer.close()
		writer = FileWriter(fileName, true)
		this.asList().forEach { writer.write("$it\n") }
		writer.close()
	}
}
