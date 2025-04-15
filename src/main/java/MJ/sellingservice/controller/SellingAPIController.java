package MJ.sellingservice.controller;

import MJ.sellingservice.dto.response.ErrorResponse;
import MJ.sellingservice.service.SellingAPIService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/info")
public class SellingAPIController {

  private final SellingAPIService sellingService;

  @GetMapping
  public ResponseEntity<?> getAllList(){
    try{
      return ResponseEntity.status(HttpStatus.OK).body(sellingService.getAllList());
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
          LocalDateTime.now(),e.getMessage()
      ));
    }
  }

  @GetMapping("/market")
  public ResponseEntity<?> getAllListSortedByMarket(){
    try{
      return ResponseEntity.status(HttpStatus.OK).body(sellingService.sortByMarket());
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
          LocalDateTime.now(),e.getMessage()
      ));
    }
  }

  @GetMapping("/product")
  public ResponseEntity<?> getAllListSortedByProduct(){
    try{
      return ResponseEntity.status(HttpStatus.OK).body(sellingService.sortByProduct());
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
          LocalDateTime.now(),e.getMessage()
      ));
    }
  }

  @GetMapping("/market/{marketName}")
  public ResponseEntity<?> findWithMarketName(@PathVariable String marketName){
    try{
      return ResponseEntity.status(HttpStatus.OK).body(sellingService.findWithMarket(marketName));
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
          LocalDateTime.now(),e.getMessage()
      ));
    }
  }

  @GetMapping("/product/{productName}")
  public ResponseEntity<?> findWithProductName(@PathVariable String productName){
    try{
      return ResponseEntity.status(HttpStatus.OK).body(sellingService.findWithProduct(productName));
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
          LocalDateTime.now(),e.getMessage()
      ));
    }
  }
}
