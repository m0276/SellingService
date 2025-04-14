package MJ.sellingservice.service;


import MJ.sellingservice.dto.SellingDto;
import MJ.sellingservice.exception.NoCorrectUrlException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  private final ObjectMapper objectMapper = new ObjectMapper();

  private Map<String,String> market = new HashMap<>();
  private Map<String,String> product = new HashMap<>();

  public List<SellingDto> getAllList(){
    StringBuilder sb = new StringBuilder(url);
    sb.append("?serviceKey=").append(serviceKey);
    try{
      URI requestUrl = new URI(sb.toString());

      ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);

      JsonNode root = objectMapper.readTree(response.getBody());

      JsonNode itemArray = root
          .path("response")
          .path("body")
          .path("items")
          .path("item");

      List<SellingDto> items = new ArrayList<>();

      if (itemArray.isArray()) {
        for (JsonNode itemNode : itemArray) {
          SellingDto dto = objectMapper.treeToValue(itemNode, SellingDto.class);
          market.put(dto.getWhsl_mrkt_nm(), dto.getWhsl_mrkt_cd());
          product.put(dto.getGds_mclsf_nm(),dto.getGds_mclsf_nm());
          items.add(dto);
        }
      }

      return items;

    } catch (URISyntaxException e) {
      throw new NoCorrectUrlException("해당 경로가 정확하지 않습니다. 다시 확인 후 시도해 주세요.");
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  // 공판장 별 날짜로 정렬
  // TODO : 생각해 볼 것 -> 거리 별로 하는 것은 어떠할지?
  public List<SellingDto> sortByMarket(){
    List<SellingDto> list = getAllList();
    list.sort(Comparator.comparing(SellingDto::getWhsl_mrkt_cd)
        .thenComparing(SellingDto::getScsbd_dt));

    return list;
  }

  // 물품 중분류와 소분류 별 정렬
  public List<SellingDto> sortByProduct(){
    List<SellingDto> list = getAllList();
    list.sort(Comparator.comparing(SellingDto::getGds_mclsf_cd)
        .thenComparing(SellingDto::getGds_sclsf_cd));

    return list;
  }
}
