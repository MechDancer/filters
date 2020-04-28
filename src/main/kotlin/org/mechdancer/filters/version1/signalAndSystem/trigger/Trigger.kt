package org.mechdancer.filters.version1.signalAndSystem.trigger

import org.mechdancer.filters.version1.signalAndSystem.IController

abstract class Trigger : IController<Boolean, Boolean> {
	abstract val finish: Boolean
	abstract fun reset()
}
