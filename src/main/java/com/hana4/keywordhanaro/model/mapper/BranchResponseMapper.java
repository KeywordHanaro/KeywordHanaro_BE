package com.hana4.keywordhanaro.model.mapper;

import java.util.Map;

import com.hana4.keywordhanaro.model.dto.BranchDto;

public class BranchResponseMapper {
	public static BranchDto toDto(Map<String, Object> document) {
		if (document == null) {
			return null;
		}
		return BranchDto.builder()
			.id(document.get("id").toString())
			.placeName(document.get("place_name").toString())
			.addressName(document.get("address_name").toString())
			.distance(document.get("distance").toString())
			.phone(document.get("phone").toString())
			.build();
	}
}
