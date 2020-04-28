package org.mechdancer.filters.version1.functional.pid

fun pid(p: Parameters, s: State, e: Double) =
		(s.sum + e).let { sum ->
			State(sum, e) to p.k * (e + p.ki * sum + p.kd * (e - s.last))
		}
