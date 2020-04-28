package org.mechdancer.filters.version1.signalAndSystem

import org.mechdancer.filters.version1.signalAndSystem.signals.Signal
import org.mechdancer.filters.version1.signalAndSystem.signals.impl.EnergySignal
import org.mechdancer.filters.version1.signalAndSystem.signals.impl.SignalImpl

sealed class PulseResponseSystem<T : Signal>(protected val f: T) : Cloneable {
	lateinit var result: T
		protected set

	operator fun plus(other: PulseResponseSystem<*>): PulseResponseSystem<*> =
			if (this is FIR && other is FIR) FIR((this.f + other.f) as EnergySignal)
			else IIR((this.f + other.f) as SignalImpl)

	operator fun times(other: PulseResponseSystem<*>): PulseResponseSystem<*> =
			if (this is FIR && other is FIR) FIR((this.f * other.f) as EnergySignal)
			else IIR((this.f * other.f) as SignalImpl)

	class FIR(es: EnergySignal) : PulseResponseSystem<EnergySignal>(es) {
		val isCausal get() = f.origin >= 0

		init {
			result = Signal.square(f.range, .0)
		}

		operator fun invoke(s: EnergySignal): EnergySignal {
			result = (result + f * s) as EnergySignal
			return result
		}


		override fun clone() = FIR(f)
	}

	class IIR(ns: Signal) : PulseResponseSystem<Signal>(ns) {
		init {
			result = Signal.signal { .0 }
		}

		operator fun invoke(s: EnergySignal): Signal {
			result += f * s
			return result
		}

		override fun clone() = IIR(f)
	}

	companion object {
		fun delay(d: Int = 1) = FIR(Signal.pulse(position = d))
		fun multiplier(k: Double) = FIR(Signal.pulse(height = k))
		fun differentiator() = FIR(Signal.signal(1.0, -1.0))
		fun integrator() = IIR(Signal.step(1))
	}
}
