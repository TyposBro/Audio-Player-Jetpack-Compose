package hoods.com.audioplayer.data.repository

import hoods.com.audioplayer.data.ContentResolverHelper
import hoods.com.audioplayer.data.model.Audio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioRepository @Inject constructor(private val contentResolverHelper: ContentResolverHelper) {
    suspend fun getAudio(): List<Audio> = withContext(
        Dispatchers.IO
    ) {
        contentResolverHelper.getAudioWorker()
    }

}