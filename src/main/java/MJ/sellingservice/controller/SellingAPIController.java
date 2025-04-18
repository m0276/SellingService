package MJ.sellingservice.controller;

import MJ.sellingservice.dto.SellingDto;
import MJ.sellingservice.dto.response.ErrorResponse;
import MJ.sellingservice.service.SellingAPIService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/infos")
public class SellingAPIController {

  private final SellingAPIService sellingService;

  @GetMapping( produces = "application/json")
  public ResponseEntity<List<SellingDto>> getAllList(){
      return ResponseEntity.status(HttpStatus.OK).body(sellingService.getAllList());
  }

  @GetMapping(value = "/market", produces = "application/json")
  public ResponseEntity<List<SellingDto>> getAllListSortedByMarket(){
      return ResponseEntity.status(HttpStatus.OK).body(sellingService.sortByMarket());
  }

  @GetMapping("/product")
  public ResponseEntity<List<SellingDto>> getAllListSortedByProduct(){
      return ResponseEntity.status(HttpStatus.OK).body(sellingService.sortByProduct());
  }

  @GetMapping("/market/{marketName}")
  public ResponseEntity<List<SellingDto>> findWithMarketName(@PathVariable String marketName){
      return ResponseEntity.status(HttpStatus.OK).body(sellingService.findWithMarket(marketName));
  }

  @GetMapping("/product/{productName}")
  public ResponseEntity<List<SellingDto>> findWithProductName(@PathVariable String productName){
      return ResponseEntity.status(HttpStatus.OK).body(sellingService.findWithProduct(productName));
  }
}
