package org.mechdancer.filters.version1.signalAndSystem.walker.impl

import org.mechdancer.filters.version1.signalAndSystem.walker.Walker

class IntWalker(private val range: IntRange) : Walker<Int>() {
	override var default = pointer
		set(value) {
			if (value in range) field = value
			else throw RuntimeException("默认值不在范围内！")
		}
	override val current get() = pointer
	override fun invoke(data: Action) = map(data, range)
	override fun reset() = run { pointer = default }
}
