package com.techpopup.musicpopup.ui.music

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.techpopup.musicpopup.R
import com.techpopup.musicpopup.application.base.AmActivity
import com.techpopup.musicpopup.business.viewmodel.SearchViewModel
import com.techpopup.musicpopup.ui.music.adapter.MusicAdapter
import com.techpopup.musicpopup.util.common.AmLogs
import com.techpopup.musicpopup.util.common.AmSearchWatcher
import com.techpopup.musicpopup.util.ext.*
import kotlinx.android.synthetic.main.view_music_home.*
import java.util.*

class MusicActivity : AmActivity(R.layout.view_music_home, R.string.text_home, true),
    MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, Runnable {

    private var latestQuery = emptyString()
    private var mediaPlayer: MediaPlayer? = null

    private var searchViewModel: SearchViewModel? = null
    private var songAdapter: MusicAdapter? = null

    override fun data() {
        super.data()

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        createPlayer()
        createSongView()
    }

    private fun createSongView() {
        if (songAdapter == null) {
            songAdapter = MusicAdapter(
                Glide.with(this), this, mutableListOf(),
                itemClick = { position, song ->
                    song?.preview?.let { uri ->
                        playMedia(uri, artist = song.artistName, title = song.trackName, artwork = song.artwork)
                        position?.let { index -> songAdapter?.isSelected(index) }
                    }
                }
            )

            val linearLayoutManager = LinearLayoutManager(this)

            lists?.apply {
                setHasFixedSize(true)
                layoutManager = linearLayoutManager
                adapter = songAdapter

                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }
        }
    }

    override fun view() {
        super.view()
    }


    override fun observer(activity: AmActivity) {
        super.observer(activity)

        searchViewModel?.songs?.observe(this, { raw ->

            raw?.let { response ->
                componentLoader?.isVisible = response.status.isLoading()

                val error = response.meta?.isError(activity) { message ->
                    message(message)
                } ?: false

                componentError?.isVisible = error

                response.payload?.let { items ->
                    items.results?.let {
                        songAdapter?.addAll(
                            it
                        )
                    } ?: run {
                        message("No Result")
                    }
                }
            }
        })
    }

    private fun playMedia(uri: String, title: String?, artwork: String?, artist: String?) {
        val radius = resources.getDimension(R.dimen.am_radius)
        imageArtWorkItem?.shapeAppearanceModel?.toBuilder()?.setAllCornerSizes(radius)?.build()?.let { imageArtWorkItem?.setShapeAppearanceModel(it) }

        imageArtWorkItem?.isVisible = true
        bottomNavigationView?.isVisible = true
        songTitleItem?.text = title
        songArtistItem?.text = artist
        imageArtWorkItem?.imageFlat(Glide.with(this), artwork)

        mediaPlayer?.apply {
            reset()
            setDataSource(uri)
            prepareAsync()
            songLoader?.isVisible = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer?.start()
        songLoader?.isVisible = false
        isPlaying(true)

        seekBar?.apply {
            progress = 0
            max = mediaPlayer?.duration ?: 0

            Thread(this@MusicActivity).start()
        }

    }

    private fun isPlaying(state: Boolean) {
        buttonPlay?.isVisible = !state
        buttonPause?.isVisible = state

        if (state) {
            Thread(this@MusicActivity).start()
        }
    }

    private fun createPlayer() {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

        }
    }

    override fun listener() {
        super.listener()

        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.setOnBufferingUpdateListener(this)

        buttonPause?.setOnClickListener {
            mediaPlayer?.pause()
            isPlaying(false)
        }

        buttonPlay?.setOnClickListener {
            mediaPlayer?.start()
            isPlaying(true)
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val currentPosition = mediaPlayer?.currentPosition ?: 0
                val total = mediaPlayer?.duration ?: 0

                songTime?.text = millisecondsToTime(currentPosition.toLong())

                if (currentPosition == total) {
                    mediaPlayer?.stop()
                    seekBar?.progress = 0
                    isPlaying(false)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mediaPlayer?.seekTo(seekBar?.progress ?: 0)
            }

        })

        componentError?.findViewById<AppCompatTextView>(R.id.buttonTryAgain)?.setOnClickListener {
            searchViewModel?.retrySearch()
        }


        swipeLayout?.setOnRefreshListener {
            swipeLayout?.isRefreshing = false
            songAdapter?.removeAll()
            searchViewModel?.retrySearch()
        }

        inputSearch?.apply {
            addTextChangedListener(
                AmSearchWatcher(
                    searchText = this,
                    afterTextChanged = { input ->
                        if (input.isNullOrEmpty()) {
                            setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_search,
                                0
                            )
                        } else {
                            setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_cancel,
                                0
                            )
                        }

                        loadSearch(input.toString().toLowerCase(Locale.ROOT).replace(" ", "+"))
                    })
            )

            onRightDrawableClicked {
                it.text.clear()
            }
        }
    }

    private fun loadSearch(query: String) {
        songAdapter?.removeAll()

        if (query.isNotEmpty() && query.length > 3) {
            if (latestQuery != query) {
                latestQuery = query
                searchViewModel?.search(query)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun run() {
        var currentPosition = mediaPlayer?.currentPosition ?: 0
        val total = mediaPlayer?.duration ?: 0

        while (mediaPlayer != null && mediaPlayer!!.isPlaying && currentPosition < total) {
            currentPosition = try {
                Thread.sleep(1000)
                mediaPlayer?.currentPosition ?: 0
            } catch (e: InterruptedException) {
                return
            } catch (e: Exception) {
                return
            }

            seekBar?.progress = currentPosition
        }
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        songLoader?.isVisible = percent < 100
        AmLogs.e("Percetage music $percent")
    }

}