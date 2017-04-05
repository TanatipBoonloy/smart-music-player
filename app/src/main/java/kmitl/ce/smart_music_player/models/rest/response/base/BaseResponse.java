package kmitl.ce.smart_music_player.models.rest.response.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


/**
 * Created by Jo on 4/4/2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse<T> {

    @JsonProperty("code")
    private int code;

    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private T data;
}
