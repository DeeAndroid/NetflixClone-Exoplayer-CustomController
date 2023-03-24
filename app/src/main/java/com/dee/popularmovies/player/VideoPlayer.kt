package com.dee.popularmovies.player

import android.content.Context
import android.net.Uri
import android.os.Handler
import com.dee.popularmovies.di.App
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.video.VideoSize

class VideoPlayer(context: Context?, INCREMENT_MILLIS: Long) : TimeBar.OnScrubListener {
    private val progressHandler: Handler
    var player: ExoPlayer?
    private var mUpdateListener: OnProgressUpdateListener? = null
    private var progressUpdater: Runnable? = null
    private lateinit var mHttpDataSourceFactory: HttpDataSource.Factory
    private lateinit var mDefaultDataSourceFactory: DefaultDataSourceFactory
    private lateinit var mCacheDataSourceFactory: DataSource.Factory
    private val cache: SimpleCache = App.cache

    /** Init class for initialisation of player */
    init {
        player = ExoPlayer.Builder(context!!).setSeekBackIncrementMs(INCREMENT_MILLIS)
            .setSeekForwardIncrementMs(INCREMENT_MILLIS).build()
        progressHandler = Handler()
    }

    /** Init media source to get prepared */
    fun initMediaSource(
        context: Context?, uri: Uri? ) {


        mHttpDataSourceFactory =
            DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)

        this.mDefaultDataSourceFactory = DefaultDataSourceFactory(
            context!!, mHttpDataSourceFactory
        )

        mCacheDataSourceFactory = CacheDataSource.Factory().setCache(cache)
            .setUpstreamDataSourceFactory(mHttpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)


        val videoUri = Uri.parse(uri.toString())

        // create a media item.
        val mediaItem =
            MediaItem.Builder().setUri(videoUri).setMimeType(MimeTypes.APPLICATION_MP4).build()

        // Create a media source and pass the media item
        val mediaSource = ProgressiveMediaSource.Factory(
            DefaultDataSource.Factory(context) // <- context
        ).createMediaSource(mediaItem)

        player?.setMediaSource(mediaSource, true)
        player?.prepare()



        player?.addListener(object : Player.Listener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                updateProgress()
            }


            override fun onLoadingChanged(isLoading: Boolean) {}
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                updateProgress()
                if (mUpdateListener != null) mUpdateListener!!.onPlayerStateChanged(playbackState)
            }

            override fun onRepeatModeChanged(repeatMode: Int) {}
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
            override fun onPositionDiscontinuity(reason: Int) {
                updateProgress()
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
            override fun onSeekProcessed() {}
            override fun onVideoSizeChanged(videoSize: VideoSize) {
                if (mUpdateListener != null && player != null) {
                    mUpdateListener!!.onFirstTimeUpdate(player!!.duration, player!!.currentPosition)
                }
            }

            override fun onRenderedFirstFrame() {}


        })
    }

    /** stop function to stop the player */
    fun stop() {
        if (player!!.isPlaying) {
            if (player != null) {
                player!!.stop()
            }
        }
    }

    /** play function to start the player */
    fun play() {
        if (!player!!.isPlaying) {
            player!!.playWhenReady = true
            removeUpdater()
        }
    }

    /** pause function to pause the player */
    fun pause() {
        player!!.playWhenReady = false
        if (!player!!.isPlaying) {
            removeUpdater()
        }
    }

    /** release the player whenever not needed */
    fun release() {
        player!!.release()
        removeUpdater()
        player = null
    }


    /** getting current time of the player */
    fun currentTime(): Long {
        return player!!.currentPosition
    }

    val isPlaying: Boolean
        get() = player!!.playWhenReady


    override fun onScrubStart(timeBar: TimeBar, position: Long) {}
    override fun onScrubMove(timeBar: TimeBar, position: Long) {
        seekTo(position)
        updateProgress()
    }

    override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
        seekTo(position)
        updateProgress()
    }

    /** updateProgress function to update the progress on the Activity/Fragments */
    private fun updateProgress() {
        if (mUpdateListener != null) {
            mUpdateListener!!.onProgressUpdate(
                player!!.currentPosition,
                if (player!!.duration == C.TIME_UNSET) 0L else player!!.duration,
                player!!.bufferedPosition
            )
        }
        initUpdateTimer()
    }

    private fun initUpdateTimer() {
        val position = player!!.currentPosition
        val playbackState = player!!.playbackState
        var delayMs: Long
        if (playbackState != ExoPlayer.STATE_IDLE && playbackState != ExoPlayer.STATE_ENDED) {
            if (player!!.playWhenReady && playbackState == ExoPlayer.STATE_READY) {
                delayMs = 100 - position % 100
                if (delayMs < 20) {
                    delayMs += 100
                }
            } else {
                delayMs = 100
            }
            removeUpdater()
            progressUpdater = Runnable { updateProgress() }
            progressHandler.postDelayed(progressUpdater!!, delayMs)
        }
    }

    private fun removeUpdater() {
        if (progressUpdater != null) progressHandler.removeCallbacks(progressUpdater!!)
    }

    /** seekTo the particular time of the video */
    fun seekTo(position: Long) {
        player!!.seekTo(position)
    }


    /** interface used to communicate with the Activity or Fragments */
    interface OnProgressUpdateListener {
        fun onProgressUpdate(currentPosition: Long, duration: Long, bufferedPosition: Long)
        fun onFirstTimeUpdate(duration: Long, currentPosition: Long)
        fun onPlayerStateChanged(playbackState: Int)
    }
}