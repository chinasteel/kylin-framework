package com.steel.kylin;

import com.alibaba.fastjson.JSON;
import com.steel.kylin.dto.KylinPageRequestDTO;
import com.steel.kylin.dto.KylinRequestDTO;
import com.steel.kylin.dto.KylinResponseDTO;
import com.steel.kylin.util.HumpUtils;
import com.steel.kylin.util.PropertyPlaceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.text.WordUtils;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void listTest() throws IllegalAccessException, InstantiationException {
        KylinResponseDTO kylinResponseDTO = new KylinResponseDTO();
        kylinResponseDTO.setAffectedRowCount(10L);
        List<KylinResponseDTO> list = new ArrayList<>();

        list.add(kylinResponseDTO);
        String string = JSON.toJSONString(list);
        Type type = ((ParameterizedType) list.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Object object = JSON.parseObject(string, list.getClass());
        System.out.println(object);
        System.out.println(type);
        System.out.println(Arrays.toString(list.getClass().getGenericInterfaces()));
        System.out.println(TypeUtils.getArrayComponentType(list.getClass()));
        System.out.println(TypeUtils.genericArrayType(list.getClass()).getGenericComponentType());
        System.out.println(Collection.class.isAssignableFrom(list.getClass()));
        System.out.println(Collection.class.isAssignableFrom(kylinResponseDTO.getClass()));
        Map<String, Object> map = new HashMap<>();
        map.put("totalScanCount", 100L);
        BeanUtils.copyProperties(map, kylinResponseDTO);
        System.out.println(JSON.toJSONString(kylinResponseDTO));
        Properties properties = new Properties();
        properties.putAll(map);
        properties.put("name", "steel");
        System.out.println(PropertyPlaceUtils.resolvePlaceholders("aa ${name} cc.", properties));

        KylinPageRequestDTO<Object> kylinPageRequestDTO = new KylinPageRequestDTO<>();
        kylinPageRequestDTO.setPageNum(1);
        kylinPageRequestDTO.setPageSize(10);
        KylinRequestDTO<Object> kylinRequestDTO = kylinPageRequestDTO;
        System.out.println(kylinRequestDTO);
    }

    @Test
    public void strTest() {
        String stringResult = "STAT_DATE_strDATE";
        System.out.println(WordUtils.uncapitalize(stringResult));
        System.out.println(WordUtils.uncapitalize(StringUtils.remove(WordUtils.capitalizeFully(stringResult, '_'), "_")));
        System.out.println(HumpUtils.hump("PV"));
    }


}
