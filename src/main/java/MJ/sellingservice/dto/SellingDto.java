package MJ.sellingservice.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellingDto {
  String corp_cd; // 법인
  String corp_gds_cd;
  String corp_gds_item_nm;
  String corp_gds_vrty_nm;
  String corp_nm;
  String gds_lclsf_cd; // 품목 코드
  String gds_lclsf_nm;
  String gds_mclsf_cd;
  String gds_mclsf_nm;
  String gds_sclsf_cd;
  String gds_sclsf_nm;
  String pkg_cd; // 포장 코드
  String pkg_nm;
  String plor_cd; // 산지 코드
  String plor_nm;
  String qty; // 총 수량
  String scsbd_dt; // 낙찰일
  String scsbd_prc; // 낙찰가
  String spm_no; // 상품 관리 번호
  String trd_clcln_ymd; // 정산일
  String trd_se; //거래 유형
  String unit_cd; // 단위
  String unit_nm;
  String unit_qty; // 한 단위 당 수량
  String whsl_mrkt_cd; // 도매시장
  String whsl_mrkt_nm;

}


//whsl_mrkt_cd : 도매시장 코드
//corp_cd : 법인 코드
//unit_cd : 단위 코드
//sz_cd : 크기 코드
//plor_cd : 산지 코드
//pkg_cd : 포장 코드
//grd_cd : 등급 코드
//gds_lclsf_cd : 품목 코드 (대분류)
//gds_mclsf_cd : 품목 코드 (중분류)
//gds_sclsf_cd : 품목 코드 (소분류)