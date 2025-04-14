package MJ.sellingservice.mapper;


import MJ.sellingservice.domain.User;
import MJ.sellingservice.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);
}
