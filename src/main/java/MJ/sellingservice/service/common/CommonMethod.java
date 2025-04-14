package MJ.sellingservice.service.common;

public interface CommonMethod<dto,request> {
  public dto save (request request);
  public dto update(request request);
  public void delete(request request);
  public dto get(request request);
}
