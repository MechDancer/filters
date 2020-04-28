package org.mechdancer.filters.version1.signalAndSystem

import java.util.concurrent.ConcurrentLinkedQueue

class DiscreteDelay(private val t: Int) : IMemorableOnlineSystem {
	init {
		if (t < 0) throw RuntimeException("时延不能小于0！")
	}

	private val queue = ConcurrentLinkedQueue<Double>()

	override fun invoke(data: Double): Double {
		queue.offer(data)
		return queue.run {
			if (size > t) poll()
			else peek()
		}
	}

	override fun reset() {
		queue.clear()
	}
}
