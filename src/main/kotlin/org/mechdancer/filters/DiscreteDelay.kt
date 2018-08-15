package org.mechdancer.filters

import java.util.*

class DiscreteDelay(private val t: Int) : IMemorableOnlineSystem {
	init {
		if (t < 0) throw RuntimeException("时延不能小于0！")
	}

	private var queue: Queue<Double> = LinkedList()

	override fun invoke(data: Double): Double {
		queue.offer(data)
		return queue.run {
			if (size > t) poll()
			else peek()
		}
	}

	override fun reset() {
		queue = LinkedList()
	}
}