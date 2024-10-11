package app.domain.util.time

import java.time.*

fun parseMonth(monthIndex: Int): String {
    return when (monthIndex) {
        0 -> "January"
        1 -> "February"
        2 -> "March"
        3 -> "April"
        4 -> "May"
        5 -> "June"
        6 -> "July"
        7 -> "August"
        8 -> "September"
        9 -> "October"
        10 -> "November"
        else -> "December"
    }
}

fun getMonthDays(monthIndex: Int, year: Int): Int {
    return when (monthIndex) {
        0, 2, 4, 6, 7, 9, 11 -> 31
        3, 5, 8, 10 -> 30
        else -> if (isLeapYear(year)) 29 else 28
    }
}

fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)
}

fun compareDates(d1: LocalDateTime, d2: LocalDateTime): Int {
    return if (d1.year == d2.year && d1.month == d2.month && d1.dayOfMonth == d2.dayOfMonth && d1.hour == d2.hour && d1.minute == d2.minute) {
        0
    } else if (d1.isAfter(d2)) {
        1
    } else {
        -1
    }
}

fun List<LocalDateTime>.sort(): List<LocalDateTime> {
    return sortedWith { o1, o2 -> compareDates(o1, o2) }
}