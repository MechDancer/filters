package org.mechdancer.filters.trigger.impl

import org.mechdancer.filters.trigger.Trigger
import org.mechdancer.filters.walker.Walker
import org.mechdancer.filters.walker.impl.IntWalker

class Counter(private val limit: Int) : Trigger() {
	private val walker = IntWalker(0..limit)
	override val finish get() = walker.current == limit
	override fun invoke(data: Boolean) =
			run { if (data) walker(Walker.Action.Next) else walker.reset(); finish }

	override fun reset() = walker.reset()
	override fun toString() = "$finish(${walker.current}/$limit)"
}