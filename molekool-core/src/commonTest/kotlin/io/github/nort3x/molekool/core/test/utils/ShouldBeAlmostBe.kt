package io.github.nort3x.molekool.core.test.utils

import io.github.nort3x.molekool.core.geomertry.point.Point
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

fun beAlmost(expect: Point) =
    object : Matcher<Point> {
        override fun test(value: Point) = MatcherResult(
            (value - expect).norm < 10e-5,
            { "$value should be almost $expect" },
            { "$value should not be near $expect" },
        )
    }

infix fun Point.shouldBeAlmostBe(other: Point) =
    this should beAlmost(other)
