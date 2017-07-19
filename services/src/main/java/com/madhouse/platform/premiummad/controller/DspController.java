package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.DspDto;
import com.madhouse.platform.premiummad.dto.MediaDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Dsp;
import com.madhouse.platform.premiummad.service.IDspService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.util.StringUtils;
import com.madhouse.platform.premiummad.validator.Update;
import com.madhouse.platform.premiummad.validator.UpdateStatus;

@RestController
@RequestMapping("/dsp")
public class DspController {
	
	@Autowired
	private IDspService dspService;
	
	@RequestMapping("/list")
    public ResponseDto<MediaDto> list(@RequestParam(value="ids", required=false) String mediaIds,
    		@RequestParam(value="userId", required=false) Integer userIdByGet,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) throws Exception {
//		//获得userId，可以从url中获得（方便通过get请求获取数据），更为一般的是从requestHeader里获取
//		if(userIdByGet != null){ //优先获取get请求的userId参数
//			userId = userIdByGet;
//		}
//		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, mediaIds);
//		String returnedMediaIds = StringUtils.getIdsStr(mediaIdList);
//		return listByMediaIds(returnedMediaIds);
		
		return null;
    }
	
	/**
	 * 创建dsp
	 * @param dspDto
	 * @return
	 */
	@RequestMapping("/create")
    public ResponseDto<DspDto> addDsp(@RequestBody DspDto dspDto,
    		@RequestHeader(value="X-FROM", required=false) String xFrom) {
		String fieldName = BeanUtils.hasEmptyField(dspDto);
        if (fieldName != null)
            return ResponseUtils.response(StatusCode.SC20001, null, fieldName + " cannot be null");
        Integer count = dspService.checkName(dspDto.getName().trim());
        if (count > 0) //检查名称
            return ResponseUtils.response(StatusCode.SC20302,null);
        Dsp dsp = new Dsp();
        BeanUtils.copyProperties(dspDto, dsp);
        BeanUtils.setCreateParam(dsp);
        dspService.insertWithParamsProcess(dsp, xFrom);
        List<DspDto> result = convertResult(dsp, new DspDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
	}
	
	/**
	 * 获得某个dsp
	 * @param id
	 * @return
	 */
	@RequestMapping("/detail")
	public ResponseDto<DspDto> getDsp(@RequestParam(value="id", required=true) Integer id) {
		Dsp dsp = dspService.queryById(id);
		List<DspDto> result = convertResult(dsp, new DspDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	/**
	 * 更新广告位
	 * @param dspDto
	 * @return
	 */
	@RequestMapping("/update")
    public ResponseDto<DspDto> updateDsp(@RequestBody @Validated(Update.class) DspDto dspDto) {
		String fieldName = BeanUtils.hasEmptyField(dspDto);
        if (fieldName != null)
            return ResponseUtils.response(StatusCode.SC20001, null, fieldName + " cannot be null");
        Dsp dsp = new Dsp();
        BeanUtils.copyProperties(dspDto, dsp);
        BeanUtils.setUpdateParam(dsp);
        dspService.update(dsp);
        List<DspDto> result = convertResult(dsp, new DspDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	/**
	 * 更新广告位状态
	 * @param dspDto
	 * @return
	 */
	@RequestMapping("/updateStatus")
    public ResponseDto<DspDto> updateDspStatus(
    		@RequestBody @Validated(UpdateStatus.class) DspDto dspDto) {
        Dsp dsp = new Dsp();
        BeanUtils.copyProperties(dspDto, dsp);
        BeanUtils.setUpdateParam(dsp);
        dspService.updateStatus(dsp);
        List<DspDto> result = convertResult(dsp, new DspDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	private List<DspDto> convertResult(Dsp dsp, DspDto dspDto) {
        BeanUtils.copyProperties(dsp,dspDto);
        List<DspDto> dspDtos = new ArrayList<>();
        dspDtos.add(dspDto);
        return dspDtos;
	}
	
}
