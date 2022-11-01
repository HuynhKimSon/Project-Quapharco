package vn.com.irtech.irbot.business.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class ApiClient extends RestTemplate {

	public ApiClient() {
		super(new BufferingClientHttpRequestFactory(new
                SimpleClientHttpRequestFactory()));
		List<ClientHttpRequestInterceptor> interceptors = this.getInterceptors();
		if (CollectionUtils.isEmpty(interceptors)) {
		    interceptors = new ArrayList<>();
		}
		interceptors.add(new ApiInterceptor());
		this.setInterceptors(interceptors);
	}
}
