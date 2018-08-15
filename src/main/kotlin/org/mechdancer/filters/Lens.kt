package org.mechdancer.filters

import java.lang.Math.abs

class Lens(private val minInput: Double, maxInput: Double,
           private val minOutput: Double, maxOutput: Double) : IOnlineSystem {
	private val reverse = minOutput > maxOutput
	private val temp = abs(maxOutput - minOutput) / (maxInput - minInput)
	private val limiter = Limiter(minInput, maxInput)

	fun runLimiter(data: Double) = limiter(data)

	override fun invoke(data: Double) = { x: Double ->
		if (reverse) minOutput - x else minOutput + x
	}((limiter(data) - minInput) * temp)
}