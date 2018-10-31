@file:Suppress("DEPRECATION")

package com.ess.core.fragment

import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import com.ess.core.R
import com.ess.core.extension.hideKeyboard
import com.ess.core.presentation.Action
import com.ess.core.presentation.AlertPresentable
import com.ess.core.presentation.KeyboardPresentable
import com.ess.core.presentation.ProgressPresentable

abstract class BaseFragment : Fragment(), ProgressPresentable, AlertPresentable,
        KeyboardPresentable {
    private var alert: AlertDialog? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun showProgress() {
        if (progressDialog != null) return

        progressDialog = ProgressDialog(context)
        progressDialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        progressDialog?.isIndeterminate = true
        progressDialog?.setCancelable(true)
        progressDialog?.show()
        progressDialog?.setContentView(R.layout.progress)
    }

    override fun hideProgress() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    override fun hideAlert() {
        alert?.dismiss()
    }

    override fun showAlert(
            title: String?,
            message: String?,
            positive: Action?,
            negative: Action?,
            neutral: Action?
    ) {
        alert?.dismiss()

        val alertBuilder = AlertDialog.Builder(requireContext())
        if (title != null && title.isNotBlank()) {
            alertBuilder.setTitle(title)
        }
        if (message != null && message.isNotBlank()) {
            alertBuilder.setMessage(message)
        }

        if (positive != null) {
            alertBuilder.setPositiveButton(positive.title, positive.listener)
        }
        if (negative != null) {
            alertBuilder.setNegativeButton(negative.title, negative.listener)
        }
        if (neutral != null) {
            alertBuilder.setNeutralButton(neutral.title, neutral.listener)
        }

        alert = alertBuilder.show()
    }

    override fun hideKeyboard() = activity?.hideKeyboard() ?: Unit

    fun showMessage(coordinatorLayout: CoordinatorLayout, message: String) {
        Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
                .show()
    }

    fun Context.getRealPathFromUri(uri: Uri): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                DocumentsContract.isDocumentUri(this, uri)) {
            if (isExternalStorageDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val split = id.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(Uri.parse(

                        "content://downloads/public_downloads"), id.toLong()
                )

                return getDataColumn(this, contentUri, null, null)

            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                val contentUri = when (type) {
                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    else -> return null
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(this, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment
            else getDataColumn(this, uri, null, null)

        } else if ("file".equals(uri.scheme, ignoreCase = true)) return uri.path

        return null
    }

    private fun getDataColumn(
            context: Context, uri: Uri, selection: String?,
            selectionArgs: Array<String>?
    ): String? {
        val column = "_data"
        val projection = arrayOf(column)

        context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                ?.takeIf { it.moveToFirst() }
                ?.use { cursor ->
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }

        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean =
            "com.android.externalstorage.documents" == uri.authority

    private fun isDownloadsDocument(uri: Uri): Boolean =
            "com.android.providers.downloads.documents" == uri.authority

    private fun isMediaDocument(uri: Uri): Boolean =
            "com.android.providers.media.documents" == uri.authority

    private fun isGooglePhotosUri(uri: Uri): Boolean =
            "com.google.android.apps.photos.content" == uri.authority

}

private val Action.listener: DialogInterface.OnClickListener?
    get() = action?.let {
        DialogInterface.OnClickListener { _, _ -> it() }
    }