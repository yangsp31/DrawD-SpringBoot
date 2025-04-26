package SJ_Project.DrawD.service.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BaseResponse<T> {
    private HttpStatus status;
    private T data;

    public  static <T> BaseResponse<T> error(HttpStatus status, T data) {
        BaseResponse<T> response = new BaseResponse<>();

        response.setStatus(status);
        response.setData(data);

        return response;
    }
}
