package org.mechdancer.filters

open class KalmanFilter(protected val a: Double,
                        protected val h: Double,
                        protected val dw: Double,
                        protected val dv: Double)
	: IMemorableOnlineSystem {
	/** 上一次的协方差  */
	private var p = 0.0

	/** 上一次的估计值  */
	private var y = 0.0

	/** 根据上一轮的估计对本轮响应进行估计  */
	protected fun estimateResponse(lastOutput: Double) = a * lastOutput

	override fun invoke(data: Double): Double {
		//估计系统响应
		val x1 = estimateResponse(y)
		//更新协方差
		val p1 = a * p * a + dw
		val kg = p1 * h / (h * p1 * h + dv)
		p = (1 - kg * h) * p1
		//进行估计
		y = x1 + kg * (data - h * x1)
		return y
	}

	override fun reset() {
		p = .0
		y = .0
	}
}