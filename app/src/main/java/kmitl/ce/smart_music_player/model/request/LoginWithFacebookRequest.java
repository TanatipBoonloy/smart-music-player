package kmitl.ce.smart_music_player.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Created by Jo on 4/4/2017.
 */
@Data
public class LoginWithFacebookRequest {
    @JsonProperty("facebook_id")
    private String facebookId;
}
