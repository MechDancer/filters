package org.mechdancer.filters.signalAndSystem.trigger

import org.mechdancer.filters.signalAndSystem.IController

abstract class Trigger : IController<Boolean, Boolean> {
	abstract val finish: Boolean
	abstract fun reset()
}
