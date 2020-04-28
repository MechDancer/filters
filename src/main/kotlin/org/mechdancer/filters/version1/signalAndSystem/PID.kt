package org.mechdancer.filters.version1.signalAndSystem

import java.lang.Math.abs

class PID(private val k: Double,
          private val ki: Double,
          private val kd: Double,
          private val integrateIArea: Double,
          private val deadArea: Double)
	: IMemorableOnlineSystem {

	/** 运行参数  */
	private var sum = 0.0
	private var last = 0.0

	override fun invoke(data: Double): Double {
		val value = abs(data)
		sum = if (value > integrateIArea) .0 else sum + data
		if (value < deadArea) sum *= .75
		//计算
		val result = data + kd * (data - last) + ki * sum
		last = data
		return k * result
	}

	/** 重置运行间参数  */
	override fun reset() {
		sum = 0.0
		last = 0.0
	}
}
