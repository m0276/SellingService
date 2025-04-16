package MJ.sellingservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import MJ.sellingservice.dto.FavoriteDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(FavoriteController.class)
class FavoriteControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  FavoriteController favoriteController;

  @Autowired
  ObjectMapper objectMapper;

  FavoriteDto favoriteDto = new FavoriteDto();

  @Test
  @WithMockUser
  void getting() throws Exception {
    mockMvc.perform(get("/api/favorites")
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void updateTest() throws Exception {
    mockMvc.perform(patch("/api/favorites")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(favoriteDto))
            .with(csrf()))
        .andExpect(status().isOk());
  }
}