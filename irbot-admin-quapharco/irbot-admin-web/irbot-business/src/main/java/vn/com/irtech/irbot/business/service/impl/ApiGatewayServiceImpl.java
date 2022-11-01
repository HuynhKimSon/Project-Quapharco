package vn.com.irtech.irbot.business.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import vn.com.irtech.irbot.business.client.ApiClient;
import vn.com.irtech.irbot.business.dto.request.ApiGetDataReq;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHdbhCnRes;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHdbhNbRes;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHdbhRes;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHddvRes;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHdxNccRes;
import vn.com.irtech.irbot.business.service.IApiGatewayService;

@Service
public class ApiGatewayServiceImpl implements IApiGatewayService {

	private static final Logger logger = LoggerFactory.getLogger(ApiGatewayServiceImpl.class);

	@Value("${api.apiUrl}")
	private String apiUrl;

	@Override
	public ApiGetDataHdbhRes getListHdbhMaster(ApiGetDataReq request) throws Exception {
		try {
			String url = apiUrl + "/api/hdbh/getListMaster";
			ApiClient apiClient = new ApiClient();

			HttpEntity<ApiGetDataReq> httpEntity = new HttpEntity<ApiGetDataReq>(request);
			ResponseEntity<ApiGetDataHdbhRes> responseEntity = apiClient.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<ApiGetDataHdbhRes>() {
					});

			if (responseEntity == null) {
				return null;
			}

			ApiGetDataHdbhRes responseData = responseEntity.getBody();

			if (responseData.getCode() == 500) {
				throw new Exception(responseData.getMsg());
			}

			return responseData;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public ApiGetDataHdbhCnRes getHdbhChiNhanh(ApiGetDataReq request) throws Exception {
		try {
			String url = apiUrl + "/api/hdbh/getListBranch";
			ApiClient apiClient = new ApiClient();

			HttpEntity<ApiGetDataReq> httpEntity = new HttpEntity<ApiGetDataReq>(request);
			ResponseEntity<ApiGetDataHdbhCnRes> responseEntity = apiClient.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<ApiGetDataHdbhCnRes>() {
					});

			if (responseEntity == null) {
				return null;
			}

			ApiGetDataHdbhCnRes responseData = responseEntity.getBody();

			if (responseData.getCode() == 500) {
				throw new Exception(responseData.getMsg());
			}

			return responseData;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public ApiGetDataHddvRes getHdDichVu(ApiGetDataReq request) throws Exception {
		try {
			String url = apiUrl + "/api/hddv/getListMaster";
			ApiClient apiClient = new ApiClient();

			HttpEntity<ApiGetDataReq> httpEntity = new HttpEntity<ApiGetDataReq>(request);
			ResponseEntity<ApiGetDataHddvRes> responseEntity = apiClient.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<ApiGetDataHddvRes>() {
					});

			if (responseEntity == null) {
				return null;
			}

			ApiGetDataHddvRes responseData = responseEntity.getBody();

			if (responseData.getCode() == 500) {
				throw new Exception(responseData.getMsg());
			}

			return responseData;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public ApiGetDataHdbhNbRes getHdbhNoiBo(ApiGetDataReq request) throws Exception {
		try {
			String url = apiUrl + "/api/hdbh-nb/getListMaster";
			ApiClient apiClient = new ApiClient();

			HttpEntity<ApiGetDataReq> httpEntity = new HttpEntity<ApiGetDataReq>(request);
			ResponseEntity<ApiGetDataHdbhNbRes> responseEntity = apiClient.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<ApiGetDataHdbhNbRes>() {
					});

			if (responseEntity == null) {
				return null;
			}

			ApiGetDataHdbhNbRes responseData = responseEntity.getBody();

			if (responseData.getCode() == 500) {
				throw new Exception(responseData.getMsg());
			}

			return responseData;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public ApiGetDataHdxNccRes getHdxNhaCungCap(ApiGetDataReq request) throws Exception {
		try {
			String url = apiUrl + "/api/hdx-ncc/getListMaster";
			ApiClient apiClient = new ApiClient();

			HttpEntity<ApiGetDataReq> httpEntity = new HttpEntity<ApiGetDataReq>(request);
			ResponseEntity<ApiGetDataHdxNccRes> responseEntity = apiClient.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<ApiGetDataHdxNccRes>() {
					});

			if (responseEntity == null) {
				return null;
			}

			ApiGetDataHdxNccRes responseData = responseEntity.getBody();

			if (responseData.getCode() == 500) {
				throw new Exception(responseData.getMsg());
			}

			return responseData;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}
}
