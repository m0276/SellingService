package MJ.sellingservice.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
  LocalDateTime time;
  String message;
}
