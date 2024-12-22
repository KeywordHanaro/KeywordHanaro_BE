package com.hana4.keywordhanaro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDto {
	public String id;
	public String placeName;
	public String addressName;
	public String distance;
	public String phone;
}
