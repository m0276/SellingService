package MJ.sellingservice.service;


import MJ.sellingservice.dto.SellingDto;
import MJ.sellingservice.exception.custom.NoCorrectException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

  // 검색용

  private Map<String,String> product = new HashMap<>();


  // 기본 정렬 : 날짜로
  public List<SellingDto> getAllList(){
    StringBuilder sb = new StringBuilder(url);
    sb.append("?serviceKey=").append(serviceKey)
        .append("&numOfRows=10000");
    List<SellingDto> list = makeList(sb);
    list.sort(Comparator.comparing(SellingDto::getScsbd_dt));

    return list;
  }

  // 공판장 별 날짜로 정렬
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

  //검색 기능

  //공판장으로 검색
  public List<SellingDto> findWithMarket(String marketName){
    Map<String,String> market = new HashMap<>();

    for(SellingDto dto : getAllList()) {
      market.put(dto.getWhsl_mrkt_nm(), dto.getWhsl_mrkt_cd());
    }

    List<SellingDto> result = new ArrayList<>();
    if(market.keySet().stream().anyMatch(key -> key.matches(".*"+ Pattern.quote(marketName)+".*"))){
      StringBuilder sb = new StringBuilder(url);
      sb.append("?serviceKey=").append(serviceKey)
          .append("&numOfRows=1000")
          .append("&cond%5Bwhsl_mrkt_cd%3A%3AEQ%5D=");

      List<String> matchingKeys = market.keySet().stream()
          .filter(key -> key.matches(".*" + Pattern.quote(marketName) + ".*"))
          .toList();
      for (String s : matchingKeys){
        StringBuilder str = new StringBuilder(sb);
        str.append(market.get(s));
        result.addAll(makeList(str));
      }

      return result;
    }
    else throw new NoCorrectException(marketName+"에 해당하는 공판장을 찾을 수 없습니다.");
  }

  //물품별 검색
  public List<SellingDto> findWithProduct(String productName){
    Map<String,String> product = new HashMap<>();

    for(SellingDto dto : getAllList()) {
      product.put(dto.getGds_mclsf_nm(),dto.getGds_mclsf_cd());
    }

    if(product.keySet().stream().anyMatch(key -> key.matches(".*"+ Pattern.quote(productName)+".*"))){
      StringBuilder sb = new StringBuilder(url);
      sb.append("?serviceKey=").append(serviceKey)
          .append("&numOfRows=1000")
          .append("&cond%5Bgds_mclsf_cd%3A%3AEQ%5D=").append(product.get(productName));
      return makeList(sb);
    }
    else throw new NoCorrectException(productName+"에 해당하는 물품을 찾을 수 없습니다.");
  }

  private List<SellingDto> makeList(StringBuilder sb){
    try {
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
          items.add(dto);
        }
      }

      return items;
    } catch (JsonProcessingException | URISyntaxException e) {
      e.printStackTrace();
      throw new NoCorrectException("조회에 실패하였습니다.");
    }
  }
}
