package edu.kh.project.main.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {

	int resetPw(Map<String, Object> map);


	int resetSecession(int inputNo2);

}
