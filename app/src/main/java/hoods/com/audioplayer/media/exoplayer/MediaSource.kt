package hoods.com.audioplayer.media.exoplayer

import hoods.com.audioplayer.data.repository.AudioRepository
import javax.inject.Inject

class MediaSource @Inject constructor(private val repository: AudioRepository) {

    private val onReadyListeners: MutableList<OnReadyListener> = mutableListOf()

    private var state: AudioSourceState = AudioSourceState.STATE_CREATED

}

typealias OnReadyListener = (Boolean) -> Unit

enum class AudioSourceState {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR,
}