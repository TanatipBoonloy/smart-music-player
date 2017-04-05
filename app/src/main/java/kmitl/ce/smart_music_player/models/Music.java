package kmitl.ce.smart_music_player.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Created by Jo on 4/4/2017.
 */
@Data
public class Music {
    @JsonProperty("song_id")
    private String song_id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("artist")
    private String artist;

    @JsonProperty("streaming_url")
    private String streaming_url;

    @JsonProperty("thumbnail_url")
    private String thumbnail_url;
}
