package com.tuannm.buoi21_boundservice;

import androidx.annotation.IntDef;

@IntDef({MediaState.IDLE,
        MediaState.PLAYING,
        MediaState.PAUSED,
        MediaState.STOPPED})
public @interface MediaState {
    int IDLE = 0;
    int PLAYING = 1;
    int PAUSED = 2;
    int STOPPED = 3;
}
