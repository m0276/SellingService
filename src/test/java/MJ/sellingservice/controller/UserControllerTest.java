package MJ.sellingservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import MJ.sellingservice.dto.UserDto;
import MJ.sellingservice.dto.request.UserRequest;
import MJ.sellingservice.repository.UserRepository;
import MJ.sellingservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(UserController.class)
class UserControllerTest{
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  UserController userController;

  @MockitoBean
  UserRepository userRepository;

  @MockitoBean
  UserService userService;

  @Autowired
  ObjectMapper objectMapper;

  UserRequest userRequest = new UserRequest("testuser", "password123", "test@example.com");

  @Test
  @WithMockUser
  void getUserInfo() throws Exception {
    mockMvc.perform(get("/api/users/me"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void joinUser() throws Exception {
    UserDto mockUserDto = new UserDto("testuser", "password123","test@example.com");
    when(userService.save(any(UserRequest.class))).thenReturn(mockUserDto);

    mockMvc.perform(post("/api/users/join")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest))
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void updateUser() throws Exception {
    UserRequest updateRequest = new UserRequest("testuser", "password12325", "test@example.com");
    mockMvc.perform(patch("/api/users/me")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRequest))
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void deleteUser() throws Exception{
    mockMvc.perform(delete("/api/users/me")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest))
            .with(csrf()))
        .andExpect(status().isOk());
  }
}