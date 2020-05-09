package org.mechdancer.filters.signal

/** 信号 */
interface Signal<T : Comparable<T>> {
    /** 获取幅值 */
    operator fun get(t: T): Double
}
