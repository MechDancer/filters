package org.mechdancer.filters

import java.lang.Math.abs

class ThresholdingProcessor(
		private val value: Double,
		private val diedArea: Double) : IOnlineSystem {

	override fun invoke(data: Double) = when {
		abs(data) < diedArea -> .0
		data > 0             -> data + value
		else                 -> data - value
	}
}