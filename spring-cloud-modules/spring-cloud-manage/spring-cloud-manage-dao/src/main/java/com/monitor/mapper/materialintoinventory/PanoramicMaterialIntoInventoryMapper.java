package com.monitor.mapper.materialintoinventory;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.cloud.core.Mapper;
import com.monitor.model.materialintoinventory.PanoramicMaterialIntoInventory;

/**
 * 
 * @author gang
 *
 */
@Repository("materialIntoInventoryMapper")
public interface PanoramicMaterialIntoInventoryMapper extends Mapper<PanoramicMaterialIntoInventory> {
	
	/**
	 * 本月累计产品出库量
	 * @param date
	 * @return
	 */
	@Select("SELECT\n" + 
			"	round(sum(value),2)\n" + 
			"FROM\n" + 
			"	panoramic_material_into_inventory\n" + 
			"WHERE\n" + 
			"	mat_type = 1\n" + 
			"AND in_out_type = 0\n" + 
			"and DATE_FORMAT(in_out_time , \"%Y-%m-%d\") BETWEEN DATE_ADD(\n" + 
			"		#{date} ,\n" + 
			"		INTERVAL - DAY(#{date}) + 1 DAY\n" + 
			"	)\n" + 
			"AND #{date}")
	double getSummaryByThisMonth(@Param("date") String date);
	
	/**
	 * 指定日期的上月累计产品出货量
	 * @param date
	 * @return
	 */
	@Select("SELECT\n" + 
			"	round(sum(VALUE) , 2)\n" + 
			"FROM\n" + 
			"	panoramic_material_into_inventory\n" + 
			"WHERE\n" + 
			"	mat_type = 1\n" + 
			"AND in_out_type = 0\n" + 
			"AND DATE_FORMAT(in_out_time , '%Y-%m') = date_format(\n" + 
			"	DATE_SUB(#{date} , INTERVAL 1 MONTH) ,\n" + 
			"	'%Y-%m'\n" + 
			")")
	double getSummaryByLastMonth(@Param("date") String date);
	
	/**
	 * 指定时间编码入出库类型查询入出库数据
	 * @param code
	 * @param type
	 * @param date
	 * @return
	 */
	@Select("SELECT\n" + 
			"	in_out_time as inOutTime,\n" + 
			"	round(VALUE , 2) as value,\n" + 
			"	person_liable as personLiable,\n" + 
			"	in_out_company as inOutCompany\n" + 
			"FROM\n" + 
			"	panoramic_material_into_inventory\n" + 
			"WHERE\n" + 
			"	in_out_type = #{type}\n" + 
			"AND CODE = #{code}\n" + 
			"AND DATE_FORMAT(in_out_time , '%Y-%m-%d') = #{date}\n" + 
			"ORDER BY\n" + 
			"	in_out_time")
	List<PanoramicMaterialIntoInventory> findMaterialValue(
			@Param("code") String code,@Param("type") String type,@Param("date") String date);
	
	/**
	 * 指定时间入出库总量以及最后更新时间
	 * @param code 
	 * @param type
	 * @param date
	 * @return
	 */
	@Select("SELECT\n" + 
			"	round(sum(VALUE),2) as value,\n" + 
			"	max(utime) as utime\n" + 
			"FROM\n" + 
			"	panoramic_material_into_inventory\n" + 
			"WHERE\n" + 
			"	in_out_type = #{type}\n" + 
			"AND CODE = #{code}\n" +
			"AND DATE_FORMAT(in_out_time , '%Y-%m-%d') = #{date}")
	PanoramicMaterialIntoInventory findSummaryByDate(@Param("code") String code, @Param("type") String type,@Param("date") String date);
}