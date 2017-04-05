package kmitl.ce.smart_music_player.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Created by Jo on 4/4/2017.
 */
@Data
public class UserResponse {
    @JsonProperty("user_id")
    private String userId;
}
