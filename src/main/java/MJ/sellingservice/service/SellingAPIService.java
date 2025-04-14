package MJ.sellingservice.service;


import MJ.sellingservice.dto.SellingDto;
import MJ.sellingservice.exception.NoCorrectUrlException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SellingAPIService {

  @Value("${end_point_current}")
  private String url;

  @Value("${curr_key}")
  private String serviceKey;

  private final RestTemplate restTemplate = new RestTemplate();

  public List<SellingDto> getAllList(){
    StringBuilder sb = new StringBuilder(url);
    sb.append("?serviceKey=").append(serviceKey);
    try{
      URI requestUrl = new URI(sb.toString());

      ResponseEntity<List<SellingDto>> response =
          restTemplate.exchange(requestUrl,
              HttpMethod.GET,
              null,
              new ParameterizedTypeReference<List<SellingDto>>() {});

      return response.getBody();

    } catch (URISyntaxException e) {
      throw new NoCorrectUrlException("해당 경로가 정확하지 않습니다. 다시 확인 후 시도해 주세요.");
    }

  }

}
