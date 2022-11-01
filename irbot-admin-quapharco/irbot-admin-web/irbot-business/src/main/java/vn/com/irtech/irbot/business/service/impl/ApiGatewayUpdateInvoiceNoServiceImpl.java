package vn.com.irtech.irbot.business.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import vn.com.irtech.irbot.business.client.ApiClient;
import vn.com.irtech.irbot.business.dto.request.ApiUpdateInvoiceNoReq;
import vn.com.irtech.irbot.business.service.IApiGatewayUpdateInoviceNoService;

@Service
public class ApiGatewayUpdateInvoiceNoServiceImpl implements IApiGatewayUpdateInoviceNoService {

	@Value("${api.apiUrl}")
	private String apiUrl;

	@Override
	public void updateInvoiceNoHdbhMaster(ApiUpdateInvoiceNoReq request) {

		String url = apiUrl + "/api/hdbh/master/updateInvoiceNo";
		ApiClient apiClient = new ApiClient();

		HttpEntity<ApiUpdateInvoiceNoReq> httpEntity = new HttpEntity<ApiUpdateInvoiceNoReq>(request);
		apiClient.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<ApiUpdateInvoiceNoReq>() {
		});
	}

	@Override
	public void updateInvoiceNoHdbhChiNhanh(ApiUpdateInvoiceNoReq request) {

		String url = apiUrl + "/api/hdbh/branch/updateInvoiceNo";
		ApiClient apiClient = new ApiClient();

		HttpEntity<ApiUpdateInvoiceNoReq> httpEntity = new HttpEntity<ApiUpdateInvoiceNoReq>(request);
		apiClient.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<ApiUpdateInvoiceNoReq>() {
		});
	}

	@Override
	public void updateInvoiceNoHdDichVu(ApiUpdateInvoiceNoReq request) {
		String url = apiUrl + "/api/hddv/updateInvoiceNo";
		ApiClient apiClient = new ApiClient();

		HttpEntity<ApiUpdateInvoiceNoReq> httpEntity = new HttpEntity<ApiUpdateInvoiceNoReq>(request);
		apiClient.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<ApiUpdateInvoiceNoReq>() {
		});
	}

	@Override
	public void updateInvoiceNoHdbhNoiBo(ApiUpdateInvoiceNoReq request) {

		String url = apiUrl + "/api/hdbh-nb/updateInvoiceNo";
		ApiClient apiClient = new ApiClient();

		HttpEntity<ApiUpdateInvoiceNoReq> httpEntity = new HttpEntity<ApiUpdateInvoiceNoReq>(request);
		apiClient.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<ApiUpdateInvoiceNoReq>() {
		});
	}

	@Override
	public void updateInvoiceNoHdxNhaCungCap(ApiUpdateInvoiceNoReq request) {

		String url = apiUrl + "/api/hdx-ncc/updateInvoiceNo";
		ApiClient apiClient = new ApiClient();

		HttpEntity<ApiUpdateInvoiceNoReq> httpEntity = new HttpEntity<ApiUpdateInvoiceNoReq>(request);
		apiClient.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<ApiUpdateInvoiceNoReq>() {
		});
	}
}
