package com.umanji.umanjiapp.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.umanji.umanjiapp.domain.entity.Entity
import kotlinx.android.parcel.Parcelize

/**
 * Data class that represents Post model.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class GroupModel(
        val group_name: String
): Entity, Parcelable