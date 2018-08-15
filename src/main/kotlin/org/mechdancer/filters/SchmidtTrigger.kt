package org.mechdancer.filters

/**施密特触发器*/
class SchmidtTrigger(
		private val downHolder: Double,
		private val upHolder: Double) : IController<Double, Boolean> {
	/**状态*/
	private var state = false
	/**本次运行状态是否改变*/
	var triggered = false
		private set

	override fun invoke(data: Double) = run {
		val temp = if (state) data >= downHolder else data > upHolder
		triggered = state != temp
		state = temp
		temp
	}
}