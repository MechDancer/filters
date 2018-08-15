package org.mechdancer.filters.trigger

import org.mechdancer.filters.IController

abstract class Trigger : IController<Boolean, Boolean> {
	abstract val finish: Boolean
	abstract fun reset()
}