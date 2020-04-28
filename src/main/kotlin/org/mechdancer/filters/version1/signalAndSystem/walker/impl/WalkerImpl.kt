package org.mechdancer.filters.version1.signalAndSystem.walker.impl

import org.mechdancer.filters.version1.signalAndSystem.walker.Walker

open class WalkerImpl<T>(private val list: List<T>) : Walker<T>() {
	override var default = list[pointer]
		set(value) {
			if (value in list) field = value
			else throw RuntimeException("默认值不在范围内！")
		}
	override val current get() = list[pointer]
	override fun invoke(data: Action) = map(data, list.indices)
	override fun reset() = run { pointer = list.indexOf(default) }
}
