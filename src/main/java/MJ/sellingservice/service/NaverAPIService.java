package MJ.sellingservice.service;

import MJ.sellingservice.dto.NaverDto;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NaverAPIService {

  @Value("${naver_request_url}")
  String startUrl;

  @Value("${client_id}")
  String clientId;

  @Value("${naver_redirect}")
  String endUrl;

  public NaverDto getUserInfo(){
    StringBuilder str = new StringBuilder(startUrl);
    str.append("response_type=code&client_id='").append(clientId)
        .append("'&redirect_uri='").append(endUrl)
        .append("'&state=1234'");
    return null;
  }

}
