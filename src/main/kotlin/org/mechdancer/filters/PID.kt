package org.mechdancer.filters

import java.lang.Math.abs

class PID(private val _k: Double,
          private val _ki: Double,
          private val _kd: Double,
          private val _integrateIArea: Double,
          private val _deadArea: Double)
	: IMemorableOnlineSystem {

	/** 运行参数  */
	private var sum = 0.0
	private var last = 0.0

	@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
	override fun invoke(error: Double): Double {
		val value = abs(error)
		sum = if (value > _integrateIArea) .0 else sum + error
		if (value < _deadArea) sum *= .75
		//计算
		val result = error + _kd * (error - last) + _ki * sum
		last = error
		return _k * result
	}

	/** 重置运行间参数  */
	override fun reset() {
		sum = 0.0
		last = 0.0
	}
}