package org.mechdancer.filters.algebra

import org.mechdancer.geometry.angle.toRad
import java.text.DecimalFormat
import kotlin.math.*

/** 复数 */
data class Complex(val re: Double, val im: Double = .0)
    : Comparable<Complex> {
    /** 模长 */
    val norm by lazy { hypot(re, im) }

    /** 幅角 */
    val arg by lazy { atan2(im, re) }

    /** 共轭 */
    val conjugate get() = Complex(re, -im)

    override fun compareTo(other: Complex) =
        norm.compareTo(other.norm)

    override fun toString(): String {
        val reText = format(re)
        val imText = format(im)
        return when {
            reText == "0" && imText == "0" -> "0"
            reText == "0"                  -> imText
            imText == "0"                  -> reText
            imText.startsWith('-')         -> "$reText - ${imText.drop(1)} i"
            else                           -> "$reText + $imText i"
        }
    }

    fun toStringAsPolar() =
        if (im == .0) format(re)
        else "${format(norm)}∠${format(arg.toRad().degree)}°"

    operator fun plus(c: Complex) = Complex(re + c.re, im + c.im)
    operator fun minus(c: Complex) = Complex(re - c.re, im - c.im)
    operator fun times(c: Complex) = Complex(re * c.re - im * c.im, re * c.im + im * c.re)
    operator fun div(c: Complex): Complex {
        val k = 1 / (c.re * c.re + c.im * c.im)
        return Complex((re * c.re + im * c.im) * k, (im * c.re - re * c.im) * k)
    }

    operator fun unaryMinus() = Complex(-re, -im)
    infix fun pow(e: Complex) =
        if (im == .0)
            polar(re.pow(e.re), if (e.im == .0) .0 else e.im * ln(re))
        else {
            val lnz = ln(norm)
            val theta = arg
            val (a, b) = e
            polar(exp(a * lnz - b * theta), a * theta + b * lnz)
        }

    @Suppress("ObjectPropertyName", "unused", "NonAsciiCharacters")
    companion object {
        private val formatter = DecimalFormat("#.###")
        private fun format(n: Double) = if (n in -9e-4..9e-4) "0" else formatter.format(n)!!

        fun polar(norm: Double, arg: Double) =
            Complex(norm * cos(arg), norm * sin(arg))

        val NaN = Complex(Double.NaN)

        val zero = Complex(.0)
        val one = Complex(1.0)

        val i = Complex(.0, 1.0)
        val e = Complex(E)
        val π = Complex(PI)
        val `+∞` = Complex(Double.POSITIVE_INFINITY)
        val `-∞` = Complex(Double.NEGATIVE_INFINITY)
        val `0` get() = zero
        val `1` get() = one
        val `-1` = Complex(-1.0)

        fun ln(x: Complex) = Complex(ln(x.norm), x.arg)
    }
}
