package com.lendbiz.gara.model.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InternalRequest extends BaseController {
	private ServiceHeader serviceHeader;
	private Object requestData;
}
