package org.mechdancer.filters

/**无记忆在线系统*/
interface IOnlineSystem : IController<Double, Double> {
	private fun operate(vararg systems: IOnlineSystem, f: (Double) -> Double)
			: IOnlineSystem {
		val temp = systems.filter { it is IMemorableOnlineSystem }
		return if (temp.isNotEmpty())
			object : IMemorableOnlineSystem {
				override fun invoke(data: Double) = f(data)
				override fun reset() = run { temp.forEach { (it as IMemorableOnlineSystem).reset() } }
			}
		else f.toController()
	}

	/**数乘*/
	operator fun times(k: Double): IOnlineSystem =
			operate(this@IOnlineSystem) { k * this@IOnlineSystem(it) }

	/**并联*/
	operator fun plus(other: IOnlineSystem): IOnlineSystem =
			operate(this@IOnlineSystem, other) { this@IOnlineSystem(it) + other(it) }

	/**串联*/
	operator fun times(right: IOnlineSystem): IOnlineSystem =
			operate(this@IOnlineSystem, right) { this@IOnlineSystem(right(it)) }

	/**负反馈*/
	fun feedBack(h: Double = -1.0): IOnlineSystem =
			if (this@IOnlineSystem is IMemorableOnlineSystem)
				object : IMemorableOnlineSystem {
					private var last = .0
					override fun invoke(data: Double) = run {
						last = this@IOnlineSystem(data + h * last)
						last
					}

					override fun reset() = this@IOnlineSystem.reset()
				}
			else
				object : IOnlineSystem {
					private var last = .0
					override fun invoke(data: Double) = run {
						last = this@IOnlineSystem(data + h * last)
						last
					}
				}
}

/**有记忆在线系统*/
interface IMemorableOnlineSystem : IOnlineSystem {
	fun reset()
}

/**数乘*/
operator fun Double.times(other: IOnlineSystem) = other * this

fun ((Double) -> Double).toController() = object : IOnlineSystem {
	override fun invoke(data: Double): Double = this@toController(data)
}