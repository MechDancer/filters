package org.mechdancer.filters.version1.signalAndSystem

import java.lang.Math.max
import java.lang.Math.min

/** 滑块滤波器  */
class Slider(private val size: Int) : IMemorableOnlineSystem {
	private val storage = mutableListOf(.0)
	private var sum = .0
	/** 滑块中的最大值  */
	var max = Double.NEGATIVE_INFINITY
		private set
	/** 滑块中的最小值  */
	var min = Double.POSITIVE_INFINITY
		private set
	/** 滑块中的最新值  */
	val last get() = storage.last()
	/** 求平均值（滑块滤波）  */
	val average get() = sum / min(size, max(storage.size, 1))
	/** 滑块中的极差  */
	val range get() = max - min
	/** 读取当前记录  */
	val record get() = storage.toDoubleArray()

	/** 向滑块置入一个值  */
	fun setValue(value: Double) {
		reset()
		invoke(value)
	}

	/** 将滑块填满一个值  */
	fun fullValue(value: Double) {
		sum = value * size
		min = value
		max = min
		for (i in storage.indices) storage[i] = value
	}

	override fun invoke(data: Double): Double {
		storage.add(data)
		val temp = storage[0]
		if (storage.size > size) {//已满，从前出队
			storage.removeAt(0)
			sum += data - temp
			when {
				data > max -> max = data //新值大于原本的最大值
				data < min -> min = data //新值小于原本的最小值
			}
			when (temp) {
				max -> max = storage.maxBy { it } ?: .0//原最大值退出
				min -> min = storage.minBy { it } ?: .0//原最小值退出
			}
		} else {//未满
			sum += data
			max = max(data, max)
			min = min(data, min)
		}
		return temp
	}

	override fun reset() {
		storage.clear()
		sum = .0
		max = .0
		min = .0
	}
}
