package org.mechdancer.filters

class LowpassFilter(private val _forgetRate: Double) : IMemorableOnlineSystem {
	var memory = .0
		private set

	override fun invoke(data: Double): Double {
		memory = memory * (1 - _forgetRate) + data * _forgetRate
		return memory
	}

	override fun reset() {
		memory = .0
	}
}