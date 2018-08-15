package org.mechdancer.filters.walker

import org.mechdancer.filters.IController

abstract class Walker<T> : IController<Walker.Action, T> {
	protected var pointer = 0
	abstract val current: T
	abstract var default: T
	abstract fun reset()
	override fun toString() = current.toString()

	protected fun map(data: Action, range: IntRange): T {
		pointer = when (data) {
			Action.Last -> Math.max(pointer - 1, range.first)
			Action.Stay -> pointer
			Action.Next -> Math.min(pointer + 1, range.last)
		}
		return current
	}

	enum class Action { Last, Stay, Next }
}