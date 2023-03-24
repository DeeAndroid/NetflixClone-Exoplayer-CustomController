package com.dee.popularmovies.ui.movies.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dee.popularmovies.R
import com.dee.popularmovies.databinding.FragmentPlayerBinding
import com.dee.popularmovies.player.VideoPlayer
import com.dee.popularmovies.utils.hideStatusNavigationBar
import com.google.android.exoplayer2.Player


class PlayerFragment : Fragment(), VideoPlayer.OnProgressUpdateListener {
    private lateinit var binding: FragmentPlayerBinding
    private var videoPlayer: VideoPlayer? = null
    private var playbackPosition: Long = 0L
    private var isPlaying: Boolean = false
    private lateinit var imageViewFullScreen: ImageView
    private lateinit var imageViewLock: ImageView
    private lateinit var imageViewBack: ImageView
    private lateinit var constraintLayoutControlUp: ConstraintLayout
    private lateinit var constraintLayoutControlBottom: ConstraintLayout

    companion object {
        private const val URL =
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
        private var isFullScreen = false
        private var isLock = false
        private const val INCREMENT_MILLIS = 5000L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        activity?.hideStatusNavigationBar()

        /** Initializing the player */
        initPlayer(Uri.parse(URL))

        return binding.root
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageViewFullScreen = view.findViewById(R.id.imageViewFullScreen)
        imageViewLock = view.findViewById(R.id.imageViewLock)
        imageViewBack = view.findViewById(R.id.ivBack)
        constraintLayoutControlUp = view.findViewById(R.id.constraintLayoutControlUp)
        constraintLayoutControlBottom = view.findViewById(R.id.constraintLayoutControlBottom)
        setLockScreen()
        setFullScreen()
        imageViewBack.setOnClickListener {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageViewFullScreen.performClick()
            } else if (isAdded) {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }

    }

    /** saving the current state of the video using saved instance */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current playback position and state of the video player
        outState.putLong("playbackPosition", videoPlayer?.currentTime() ?: 0L)
        outState.putBoolean("isPlaying", videoPlayer?.isPlaying ?: false)
    }

    /** restoring the current state of the video using onViewStateRestored */
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Restore the saved playback position and state of the video player
        savedInstanceState?.let {
            playbackPosition = it.getLong("playbackPosition", 0L)
            isPlaying = it.getBoolean("isPlaying", false)
        }
    }

    /** checking and setting the current position of the player on lifecycle changes */
    override fun onResume() {
        super.onResume()
        // Resume the video playback from the saved playback position
        videoPlayer?.seekTo(playbackPosition)
        if (isPlaying) {
            videoPlayer?.play()
        }
    }

    /** Initialising the player */
    private fun initPlayer(uri: Uri?) {
        videoPlayer = VideoPlayer(requireActivity(),INCREMENT_MILLIS)
        videoPlayer?.initMediaSource(requireContext(), uri)
        binding.video.player = videoPlayer!!.player
        videoPlayer?.play()
    }


    /** stopping and releasing the player here */
    override fun onDestroy() {
        super.onDestroy()
        // Release the video player resources
        videoPlayer?.stop()
        videoPlayer?.release()
    }

    /** pausing the player here*/
    override fun onPause() {
        super.onPause()
        videoPlayer?.pause()
    }

    override fun onProgressUpdate(currentPosition: Long, duration: Long, bufferedPosition: Long) {

    }

    override fun onFirstTimeUpdate(duration: Long, currentPosition: Long) {
    }

    /** handling progressbar on buffering and playing */
    override fun onPlayerStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_IDLE -> {
                binding.isProgressVisible = false
            }
            Player.STATE_BUFFERING -> {
                binding.isProgressVisible = true
            }
            Player.STATE_READY -> {
                binding.isProgressVisible = false
            }
            Player.STATE_ENDED -> {
                videoPlayer?.play()
                videoPlayer?.seekTo(0)
            }
        }
    }

    /** locking the controller  */
    private fun setLockScreen() {
        imageViewLock.setOnClickListener {
            if (!isLock) {
                imageViewLock.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_lock
                    )
                )
            } else {
                imageViewLock.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_lock_open
                    )
                )
            }
            isLock = !isLock
            lockScreen(isLock)
        }
    }

    /** handling the orientation */
    @SuppressLint("SourceLockedOrientationActivity")
    private fun setFullScreen() {
        imageViewFullScreen.setOnClickListener {

            if (!isFullScreen) {
                imageViewFullScreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_fullscreen_exit
                    )
                )
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                imageViewFullScreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_fullscreen
                    )
                )
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            isFullScreen = !isFullScreen
        }
    }


    /** handling the visibility while locking the screen */
    private fun lockScreen(lock: Boolean) {
        if (lock) {
            constraintLayoutControlUp.visibility = View.INVISIBLE
            constraintLayoutControlBottom.visibility = View.INVISIBLE
        } else {
            constraintLayoutControlUp.visibility = View.VISIBLE
            constraintLayoutControlBottom.visibility = View.VISIBLE
        }
    }





}
