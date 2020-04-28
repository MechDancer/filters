package org.mechdancer.filters.signal

/** 信号 */
interface Signal<T : Comparable<T>> {
    /** 获取幅值 */
    operator fun get(t: T): Double

//    /** 右移/加延时 */
//    fun delay(t: T): Signal<T, THIS>
//
//    /** 相加 */
//    operator fun plus(others: Signal<T, THIS>): Signal<T, THIS>
//
//    /** 相减 */
//    operator fun minus(others: Signal<T, THIS>): Signal<T, THIS>
//
//    /** 相乘 */
//    operator fun times(others: Signal<T, THIS>): Signal<T, THIS>
//
//    /** 数乘 */
//    operator fun times(k: Double): Signal<T, THIS>
//
//    /** 数除 */
//    operator fun div(k: Double): Signal<T, THIS>
}
