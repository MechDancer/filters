package org.mechdancer.filters.signalAndSystem.signals

import org.mechdancer.filters.signalAndSystem.signals.impl.EnergySignal
import org.mechdancer.filters.signalAndSystem.signals.impl.SignalImpl


abstract class Signal(internal val f: (Int) -> Double) {
	/**取得信号某一个位置的值*/
	operator fun get(k: Int) = f(k)

	/**取得信号某一范围内的值（作为随机信号）*/
	operator fun get(r: IntRange) = EnergySignal(r, f)

	/**左移*/
	abstract infix fun lead(n: Int): Signal

	/**右移*/
	infix fun delay(n: Int) = lead(-n)

	/**上移*/
	abstract fun rise(y: Double): Signal

	/**下移*/
	infix fun drop(y: Double) = rise(-y)

	operator fun plus(y: Double) = rise(y)
	operator fun minus(y: Double) = rise(-y)
	abstract operator fun times(k: Double): Signal
	operator fun div(k: Double) = this * (1 / k)

	/**反转*/
	abstract infix fun reverseInX(x0: Int): Signal

	/**反转*/
	abstract infix fun reverseInY(y0: Double): Signal

	abstract operator fun plus(other: Signal): Signal
	abstract operator fun minus(other: Signal): Signal
	abstract infix fun x(other: Signal): Signal
	abstract operator fun times(other: Signal): Signal

	companion object {
		fun signal(f: (Int) -> Double): Signal = SignalImpl(f)

		fun signal(range: IntRange, f: (Int) -> Double) = EnergySignal(range, f)

		fun signal(list: List<Double>, origin: Int = 0) = EnergySignal(list, origin)

		fun signal(vararg data: Double) = EnergySignal(data.asList())

		fun zeroSignal(): Signal = SignalImpl { .0 }

		/**脉冲信号*/
		fun pulse(position: Int = 0, height: Double = 1.0) = EnergySignal(position..position) { height }

		/**阶跃信号*/
		fun step(position: Int = 0, height: Double = 1.0): Signal = SignalImpl { if (it >= position) height else .0 }

		/**方波信号*/
		fun square(range: IntRange, height: Double = 1.0) = EnergySignal(range) { height }

		/**斜坡信号*/
		fun ramp(position: Int = 0, k: Double = 1.0): Signal = SignalImpl { if (it >= position) k * (it - position) else .0 }

		/**锯齿信号*/
		fun sawtooth(range: IntRange, k: Double = 1.0) = EnergySignal(range) { k * (it - range.first) }
	}
}
