package net.runner.relley.Repository


import android.os.Parcel
import android.os.Parcelable

data class Repository(
    val id: Long,
    val name: String?,
    val full_name: String?,
    val private: Boolean,
    val owner: Owner?,
    val htmlUrl: String?,
    val description: String?,
    val fork: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readLong(),
        name = parcel.readString(),
        full_name = parcel.readString(),
        private = parcel.readByte() != 0.toByte(),
        owner = parcel.readParcelable(Owner::class.java.classLoader),
        htmlUrl = parcel.readString(),
        description = parcel.readString(),
        fork = parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(full_name)
        parcel.writeByte(if (private) 1 else 0)
        parcel.writeParcelable(owner, flags)
        parcel.writeString(htmlUrl)
        parcel.writeString(description)
        parcel.writeByte(if (fork) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Repository> {
        override fun createFromParcel(parcel: Parcel): Repository {
            return Repository(parcel)
        }

        override fun newArray(size: Int): Array<Repository?> {
            return arrayOfNulls(size)
        }
    }
}

data class Owner(
    val login: String?,
    val id: Long,
    val avatar_url: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        login = parcel.readString(),
        id = parcel.readLong(),
        avatar_url = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeLong(id)
        parcel.writeString(avatar_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Owner> {
        override fun createFromParcel(parcel: Parcel): Owner {
            return Owner(parcel)
        }

        override fun newArray(size: Int): Array<Owner?> {
            return arrayOfNulls(size)
        }
    }
}