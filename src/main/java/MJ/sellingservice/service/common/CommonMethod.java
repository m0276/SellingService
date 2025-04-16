package MJ.sellingservice.service.common;

public interface CommonMethod<dto,request> {
  dto save(request request);
  dto update(request request);
  void delete(request request);
  dto get();
}
