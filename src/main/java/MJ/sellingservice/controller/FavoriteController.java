package MJ.sellingservice.controller;

import MJ.sellingservice.dto.FavoriteDto;
import MJ.sellingservice.service.FavoriteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
  private final FavoriteService favoriteService;

  @GetMapping
  public ResponseEntity<List<FavoriteDto>> get(){
    return ResponseEntity.status(HttpStatus.OK).body(favoriteService.get());
  }

  @PatchMapping
  public ResponseEntity<Void> update(@RequestBody FavoriteDto favoriteDto){
    favoriteService.checkAndSave(favoriteDto);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
