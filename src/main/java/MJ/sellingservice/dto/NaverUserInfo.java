package MJ.sellingservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NaverUserInfo {
  private String id;
  private String email;
  private String name;
}
