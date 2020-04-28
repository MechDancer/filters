package org.mechdancer.filters.version1.signalAndSystem.signals.impl


class PeriodicSignal(
		f: (Int) -> Double,
		range: IntRange
) : SignalImpl({ f((it - range.first) % (range.last - range.first + 1) + range.first) }) {
	/**从能量（有限）信号构造*/
	constructor(source: EnergySignal) : this(source.f, source.range)

	/**周期*/
	val period = range.last - range.first + 1
}
