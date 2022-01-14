package ru.netology.nmedia

object PostService {
    fun getCountText(count: Int): String =
        when {
            count < 1_000 -> count.toString()
            count < 10_000 -> getDottedNumber(count, 1_000) + "K"
            count < 1_000_000 -> (count / 1_000).toString() + "K"
            else -> getDottedNumber(count, 1_000_000) + "M"
        }

    private fun getDottedNumber(count: Int, divider: Int): String {
        val firstDivision = count / (divider / 10)
        if (firstDivision % 10 == 0) {
            return (firstDivision / 10).toString()
        }
        return (firstDivision.toDouble() / 10).toString()
    }
}