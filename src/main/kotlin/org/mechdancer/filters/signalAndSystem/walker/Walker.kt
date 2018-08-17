package org.mechdancer.filters.signalAndSystem.walker

import org.mechdancer.filters.signalAndSystem.IController
import org.mechdancer.filters.signalAndSystem.walker.Walker.Action
import org.mechdancer.filters.signalAndSystem.walker.Walker.Action.*

abstract class Walker<T> : IController<Action, T> {
	protected var pointer = 0
	abstract val current: T
	abstract var default: T
	abstract fun reset()
	override fun toString() = current.toString()

	protected fun map(data: Action, range: IntRange): T {
		pointer = when (data) {
			Last -> Math.max(pointer - 1, range.first)
			Stay -> pointer
			Next -> Math.min(pointer + 1, range.last)
		}
		return current
	}

	enum class Action { Last, Stay, Next }
}
