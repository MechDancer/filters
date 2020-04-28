package org.mechdancer.filters.version1.signalAndSystem

class LowpassFilter(private val forgetRate: Double) : IMemorableOnlineSystem {
	var memory = .0
		private set

	override fun invoke(data: Double): Double {
		memory = memory * (1 - forgetRate) + data * forgetRate
		return memory
	}

	override fun reset() {
		memory = .0
	}
}
