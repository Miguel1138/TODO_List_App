package com.miguelsantos.todewit.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val title: String,
    val description: String,
    val hour: String,
    val date: String,
    var isDone: Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 1
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun describeContents(): Int = 0

    // Parcelize the data
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeString(description)
        dest?.writeString(hour)
        dest?.writeString(date)
        dest?.writeInt(isDone)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = id

    companion object CREATOR : Parcelable.Creator<Task> {

        override fun createFromParcel(parcel: Parcel): Task = Task(parcel)

        override fun newArray(size: Int): Array<Task?> = arrayOfNulls(size)

    }

}
