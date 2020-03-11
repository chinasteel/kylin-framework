package com.steel.kylin;

import com.steel.kylin.annotion.KylinMethod;
import com.steel.kylin.convert.KylinRequestConvert;
import com.steel.kylin.convert.KylinResponseConvert;
import com.steel.kylin.dto.KylinPageRequestDTO;
import com.steel.kylin.dto.KylinRequestDTO;
import com.steel.kylin.dto.KylinResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @author steel
 * datetime 2020/1/7 14:51
 */
public class KylinRepsitoryInvocationHander implements InvocationHandler {
    private final String restUrl;
    private final String userName;
    private final String password;
    private final RestTemplate restTemplate;
	private final KylinRequestConvert kylinRequestConvert;
	private final KylinResponseConvert kylinResponseConvert;

    public KylinRepsitoryInvocationHander(String restUrl, String userName, String password, RestTemplate restTemplate,
										  KylinRequestConvert kylinRequestConvert, KylinResponseConvert kylinResponseConvert) {
        this.restUrl = restUrl;
		this.userName = userName;
		this.password = password;
		this.restTemplate = restTemplate;
		this.kylinRequestConvert = kylinRequestConvert;
		this.kylinResponseConvert = kylinResponseConvert;
	}

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            } else if (isDefaultMethod(method)) {
                return invokeDefaultMethod(proxy, method, args);
            }
        } catch (Throwable t) {
            throw Objects.requireNonNull(NestedExceptionUtils.getRootCause(t));
        }

		KylinMethod kylinMethod = method.getAnnotation(KylinMethod.class);

		HttpHeaders httpHeaders = this.getHttpHeaders();

		// 总页数处理
		this.handleKylinTotalRequest(kylinMethod, httpHeaders, args);

		// 常规内容处理
		HttpEntity<KylinRequest> httpEntity = new HttpEntity<>(this.getKylinRequest(kylinMethod, args),
			httpHeaders);
		KylinResponseDTO kylinResponseDTO = restTemplate.postForObject(restUrl, httpEntity, KylinResponseDTO.class);
		Assert.notNull(kylinResponseDTO, "no response from kylin.");
		Assert.isTrue(!kylinResponseDTO.isException(), kylinResponseDTO.getExceptionMessag());
		return this.kylinResponseConvert.convert(method, kylinResponseDTO);
	}

	private HttpHeaders getHttpHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		httpHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
		httpHeaders.add("timeStamp", String.valueOf(System.currentTimeMillis()));
		httpHeaders.add("Authorization", "Basic " +
			Base64Utils.encodeToString((this.userName + ":" + this.password).getBytes(StandardCharsets.UTF_8)));
		return httpHeaders;
	}


	private void handleKylinTotalRequest(KylinMethod kylinMethod, HttpHeaders httpHeaders, Object[] args) {
		if (!kylinMethod.total()) {
			return;
		}
		KylinRequestDTO<Object> kylinTotalRequestDTO = kylinRequestConvert.convertTotal(kylinMethod, args);

		// 总页数处理
		HttpEntity<KylinRequest> totalHttpEntity = new HttpEntity<>(this.getTotalKylinRequest(kylinTotalRequestDTO),
			httpHeaders);
		KylinResponseDTO totalKylinResponseDTO = restTemplate.postForObject(restUrl, totalHttpEntity,
			KylinResponseDTO.class);
		Assert.notNull(totalKylinResponseDTO, "no response from kylin.");
		Assert.isTrue(!totalKylinResponseDTO.isException(), totalKylinResponseDTO.getExceptionMessag());
		if (kylinTotalRequestDTO instanceof KylinPageRequestDTO) {
			List<List<String>> results = totalKylinResponseDTO.getResults();
			if (CollectionUtils.isEmpty(results)) {
				return;
			}
			List<String> totalResults = results.get(0);
			if (CollectionUtils.isEmpty(totalResults)) {
				return;
			}
			((KylinPageRequestDTO<Object>) kylinTotalRequestDTO).setTotal(Long.valueOf(totalResults.get(0)));
		}
	}

	private KylinRequest getKylinRequest(KylinMethod kylinMethod, Object[] args) {
		KylinRequestDTO<Object> kylinRequestDTO = kylinRequestConvert.convert(kylinMethod, args);
		Assert.notNull(kylinMethod, "annotation kylinMethod is null");
		KylinRequest kylinRequest;
		if (kylinRequestDTO instanceof KylinPageRequestDTO) {
			kylinRequest = this.getKylinPageRequest(kylinRequestDTO);
		} else {
			kylinRequest = new KylinRequest();
			BeanUtils.copyProperties(kylinRequestDTO, kylinRequest);
		}
		return kylinRequest;
	}

	private KylinRequest getKylinPageRequest(KylinRequestDTO<Object> kylinRequestDTO) {
		KylinPageRequestDTO<Object> kylinPageRequestDTO = (KylinPageRequestDTO<Object>) kylinRequestDTO;
		KylinRequest kylinRequest = new KylinRequest(kylinPageRequestDTO.getPageNum(), kylinPageRequestDTO.getPageSize());
		BeanUtils.copyProperties(kylinPageRequestDTO, kylinRequest);
		return kylinRequest;
	}

	private KylinRequest getTotalKylinRequest(KylinRequestDTO<Object> kylinRequestDTO) {
		KylinRequest kylinRequest = new KylinRequest();
		BeanUtils.copyProperties(kylinRequestDTO, kylinRequest);
		return kylinRequest;
	}

    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
            throws Throwable {
        final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                .getDeclaredConstructor(Class.class, int.class);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        final Class<?> declaringClass = method.getDeclaringClass();
        return constructor
                .newInstance(declaringClass,
                        MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
                                | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC)
                .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
    }

    /**
     * Backport of java.lang.reflect.Method#isDefault()
     */
    private boolean isDefaultMethod(Method method) {
        return (method.getModifiers()
                & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC
                && method.getDeclaringClass().isInterface();
    }
}
