package com.guichaguri.trackplayer.service.metadata;

import android.os.Bundle;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaSessionCompat;
import com.guichaguri.trackplayer.module.MusicEvents;
import com.guichaguri.trackplayer.service.MusicManager;
import com.guichaguri.trackplayer.service.MusicService;
import com.guichaguri.trackplayer.service.Utils;
import com.guichaguri.trackplayer.service.models.Track;
import java.util.List;

/**
 * @author Guichaguri
 */
public class ButtonEvents extends MediaSessionCompat.Callback {

    private final MusicService service;
    private final MusicManager manager;

    public ButtonEvents(MusicService service, MusicManager manager) {
        this.service = service;
        this.manager = manager;
    }

    @Override
    public void onPlay() {
        service.emit(MusicEvents.BUTTON_PLAY, null);
    }

    @Override
    public void onPause() {
        service.emit(MusicEvents.BUTTON_PAUSE, null);
    }

    @Override
    public void onStop() {
        service.emit(MusicEvents.BUTTON_STOP, null);
    }

    @Override
    public void onSkipToQueueItem(long id) {
        List<Track> tracks = manager.getPlayback().getQueue();

        for(Track track : tracks) {
            if(track.queueId != id) continue;

            Bundle bundle = new Bundle();
            bundle.putString("id", track.id);
            service.emit(MusicEvents.BUTTON_SKIP, bundle);
            break;
        }
    }

    @Override
    public void onSkipToPrevious() {
        service.emit(MusicEvents.BUTTON_SKIP_PREVIOUS, null);
    }

    @Override
    public void onSkipToNext() {
        service.emit(MusicEvents.BUTTON_SKIP_NEXT, null);
    }

    @Override
    public void onRewind() {
        Bundle bundle = new Bundle();
        bundle.putInt("interval", manager.getMetadata().getJumpInterval());
        service.emit(MusicEvents.BUTTON_JUMP_BACKWARD, bundle);
    }

    @Override
    public void onFastForward() {
        Bundle bundle = new Bundle();
        bundle.putInt("interval", manager.getMetadata().getJumpInterval());
        service.emit(MusicEvents.BUTTON_JUMP_FORWARD, bundle);
    }

    @Override
    public void onSeekTo(long pos) {
        Bundle bundle = new Bundle();
        bundle.putDouble("position", Utils.toSeconds(pos));
        service.emit(MusicEvents.BUTTON_SEEK_TO, bundle);
    }

    @Override
    public void onSetRating(RatingCompat rating) {
        Bundle bundle = new Bundle();
        Utils.setRating(bundle, "rating", rating);
        service.emit(MusicEvents.BUTTON_SET_RATING, bundle);
    }
}
