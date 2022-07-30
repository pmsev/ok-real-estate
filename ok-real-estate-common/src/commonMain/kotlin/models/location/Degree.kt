package models.location

data class Degree(private val degree: Double) {

    fun asDouble(): Double{
        return degree
    }



    companion object {
        val NONE = Degree(0.0000)
    }

}

