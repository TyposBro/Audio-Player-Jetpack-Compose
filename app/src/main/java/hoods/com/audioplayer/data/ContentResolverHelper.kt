package hoods.com.audioplayer.data

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import dagger.hilt.android.qualifiers.ApplicationContext
import hoods.com.audioplayer.data.model.Audio
import javax.inject.Inject

class ContentResolverHelper @Inject constructor(@ApplicationContext val context: Context){

    private var mCursor: Cursor? = null
    private val projection: Array<String> = arrayOf(
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.TITLE,
    )

    private var selectionClause: String? = "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ?"

    private var selectionArgs: Array<String> = arrayOf("1")

    private val sortOrder: String = "${MediaStore.Audio.AudioColumns.DISPLAY_NAME} ASC"

    @WorkerThread
    fun getAudioWorker():List<Audio>{
        return getAudio()
    }


    private fun getAudio():MutableList<Audio>{
        val audioList: MutableList<Audio> = mutableListOf()

        val mCursor: Cursor? = getCursor()

        mCursor?.use { cursor->
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)

            cursor.apply {
                if (count == 0){
                    Log.e("ContentResolverHelper", "No audio files found")
                }else{
                    while (moveToNext()){
                        val displayName = getString(displayNameColumn)
                        val id = getLong(idColumn)
                        val artist = getString(artistColumn)
                        val data = getString(dataColumn)
                        val duration = getLong(durationColumn)
                        val title = getString(titleColumn)
                        val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                        val audio = Audio(
                            displayName = displayName,
                            id = id,
                            artist = artist,
                            data = data,
                            duration = duration,
                            title = title,
                            uri = uri
                        )

                        audioList.add(audio)
                    }
                }
            }
        }

        return audioList

    }

    private fun getCursor(): Cursor? {
        val contentResolver: ContentResolver = context.contentResolver
        return contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selectionClause,
            selectionArgs,
            sortOrder
        )
    }
}