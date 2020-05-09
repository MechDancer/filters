package org.mechdancer.filters.algebra

import kotlin.math.PI

fun List<Complex>.dft(): List<Complex> {
    val w0 = Complex.e.pow((-2 * PI / size).asIm())
    return List(size) { k ->
        mapIndexed { n, x ->
            x * w0.pow((n * k).asRe())
        }.sum()
    }
}

fun List<Complex>.idft(): List<Complex> {
    val w0 = Complex.e.pow((2 * PI / size).asIm())
    return List(size) { k ->
        mapIndexed { n, x ->
            x * w0.pow((n * k).asRe()) / size.asRe()
        }.sum()
    }
}

fun Number.asRe() =
    Complex(toDouble(), .0)

fun Number.asIm() =
    Complex(.0, toDouble())

fun Iterable<Complex>.sum() =
    reduce { sum, it -> sum + it }

inline fun <T> Iterable<T>.sumByComplex(
    block: (T) -> Complex
) = fold(Complex.zero) { sum, it -> sum + block(it) }
