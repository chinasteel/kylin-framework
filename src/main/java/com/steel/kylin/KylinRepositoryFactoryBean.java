package com.steel.kylin;

import com.steel.kylin.convert.KylinRequestConvert;
import com.steel.kylin.convert.KylinResponseConvert;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * @author steel
 * datetime 2020/1/6 14:45
 */
public class KylinRepositoryFactoryBean<T> implements FactoryBean<T> {
    private Class<T> kylinRepositoryInterface;
    private String restUrl;
	private String userName;
	private String password;
    private RestTemplate restTemplate;
	private KylinRequestConvert kylinRequestConvert;
	private KylinResponseConvert kylinResponseConvert;

    public KylinRepositoryFactoryBean() {

    }

    // can't ignore.
    public KylinRepositoryFactoryBean(Class<T> kylinRepositoryInterface) {
        this.kylinRepositoryInterface = kylinRepositoryInterface;
    }

	@Override
    public T getObject() throws Exception {
        Assert.notNull(this.kylinRepositoryInterface, "kylinRepositoryInterface is null");
        KylinRepositoryProxyFactory<T> kylinRepositoryProxyFactory = new KylinRepositoryProxyFactory<>(kylinRepositoryInterface);
        return kylinRepositoryProxyFactory.newInstance(this.restUrl, this.userName, this.password, this.restTemplate,
			this.kylinRequestConvert, this.kylinResponseConvert);
    }

    @Override
    public Class<?> getObjectType() {
        return this.kylinRepositoryInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

	public String getRestUrl() {
		return restUrl;
	}

	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public KylinRequestConvert getKylinRequestConvert() {
		return kylinRequestConvert;
	}

	public void setKylinRequestConvert(KylinRequestConvert kylinRequestConvert) {
		this.kylinRequestConvert = kylinRequestConvert;
	}

	public KylinResponseConvert getKylinResponseConvert() {
		return kylinResponseConvert;
	}

	public void setKylinResponseConvert(KylinResponseConvert kylinResponseConvert) {
		this.kylinResponseConvert = kylinResponseConvert;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
