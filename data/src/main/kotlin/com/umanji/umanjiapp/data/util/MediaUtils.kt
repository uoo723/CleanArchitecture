package com.umanji.umanjiapp.data.util

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class MediaUtils @Inject constructor(private val context: Context) {

    fun getMimeType(uri: Uri): String {
        return context.contentResolver.getType(uri) ?: MimeTypeMap.getFileExtensionFromUrl(uri.path)
    }

    @SuppressLint("NewApi")
    fun getPath(uri: Uri): String {
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        var newUri = uri

        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context, newUri)) {
            when {
                isExternalStorageDocument(newUri) -> {
                    val docId = DocumentsContract.getDocumentId(newUri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    return "${Environment.getExternalStorageDirectory()}/${split[1]}"
                }
                isDownloadsDocument(newUri) -> {
                    val id = DocumentsContract.getDocumentId(newUri)
                    newUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), id.toLong())
                }
                isMediaDocument(newUri) -> {
                    val docId = DocumentsContract.getDocumentId(newUri)
                    val split = docId.split(":")
                    val type = split[0]
                    newUri = when (type) {
                        "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        else -> throw IllegalArgumentException("content type is not valid")
                    }
                    selection = "_id=?"
                    selectionArgs = arrayOf(split[1])
                }
            }
        }

        newUri.scheme?.let {
            if (it.equals("content", ignoreCase = true)) {
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor = context.contentResolver.query(newUri, projection, selection, selectionArgs, null)
                cursor.use {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    if (it.moveToFirst()) {
                        return it.getString(columnIndex)
                    }
                }
            } else if (it.equals("file", ignoreCase = true)) {
                return newUri.path
            }
        }

        throw IllegalStateException("Cannot be reached")
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}
