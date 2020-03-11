package com.steel.kylin;

import com.steel.kylin.convert.KylinRequestConvert;
import com.steel.kylin.convert.KylinResponseConvert;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Proxy;

/**
 * @author steel
 * datetime 2020/1/7 16:04
 */
public class KylinRepositoryProxyFactory<T> {
    public final Class<T> kylinRepositoryInterface;

    public KylinRepositoryProxyFactory(Class<T> kylinRepositoryInterface) {
        this.kylinRepositoryInterface = kylinRepositoryInterface;
    }

    @SuppressWarnings("unchecked")
    protected T newInstance(KylinRepsitoryInvocationHander kylinRepsitoryInvocationHander) {
        return (T) Proxy.newProxyInstance(kylinRepositoryInterface.getClassLoader(),
                new Class[]{kylinRepositoryInterface}, kylinRepsitoryInvocationHander);
    }

    public T newInstance(String restUrl, String userName, String password, RestTemplate restTemplate,
                         KylinRequestConvert kylinRequestConvert, KylinResponseConvert kylinResponseConvert) {
        Assert.notNull(restUrl, "restUrl is null");
		Assert.notNull(userName, "userName is null");
		Assert.notNull(password, "password is null");
        Assert.notNull(restTemplate, "restTemplate is null");
		Assert.notNull(kylinResponseConvert, "kylinResponseConvert is null");

        KylinRepsitoryInvocationHander kylinRepsitoryInvocationHander = new KylinRepsitoryInvocationHander(restUrl,
			userName, password, restTemplate, kylinRequestConvert, kylinResponseConvert);
        return this.newInstance(kylinRepsitoryInvocationHander);
    }

}
