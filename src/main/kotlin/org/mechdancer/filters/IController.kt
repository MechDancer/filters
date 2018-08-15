package org.mechdancer.filters

/**控制器*/
interface IController<in TI, out TO> {
	operator fun invoke(data: TI): TO

	operator fun <T> times(right: IController<T, TI>): IController<T, TO> =
			{ data: T -> this@IController(right(data)) }.toController()
}

fun <TI, TO> ((TI) -> TO).toController() = object : IController<TI, TO> {
	override operator fun invoke(data: TI): TO = this@toController(data)
}