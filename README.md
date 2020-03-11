# Kylin-framework

##解决痛点
用过kylin restapi的，我想大家应该都会特别痛苦吧，返回的数据完全不是java能hold住的，附上官网链接
http://kylin.apache.org/cn/docs/howto/howto_use_restapi.html，下面是官网返回的demo：
```
{  
    "columnMetas":[  
       {  
          "isNullable":1,
          "displaySize":0,
          "label":"CAL_DT",
          "name":"CAL_DT",
          "schemaName":null,
          "catelogName":null,
          "tableName":null,
          "precision":0,
          "scale":0,
          "columnType":91,
          "columnTypeName":"DATE",
          "readOnly":true,
          "writable":false,
          "caseSensitive":true,
          "searchable":false,
          "currency":false,
          "signed":true,
          "autoIncrement":false,
          "definitelyWritable":false
       },
       {  
          "isNullable":1,
          "displaySize":10,
          "label":"LEAF_CATEG_ID",
          "name":"LEAF_CATEG_ID",
          "schemaName":null,
          "catelogName":null,
          "tableName":null,
          "precision":10,
          "scale":0,
          "columnType":4,
          "columnTypeName":"INTEGER",
          "readOnly":true,
          "writable":false,
          "caseSensitive":true,
          "searchable":false,
          "currency":false,
          "signed":true,
          "autoIncrement":false,
          "definitelyWritable":false
       }
    ],
    "results":[  
       [  
          "2013-08-07",
          "32996",
          "15",
          "15",
          "Auction",
          "10000000",
          "49.048952730908745",
          "49.048952730908745",
          "49.048952730908745",
          "1"
       ],
       [  
          "2013-08-07",
          "43398",
          "0",
          "14",
          "ABIN",
          "10000633",
          "85.78317064220418",
          "85.78317064220418",
          "85.78317064220418",
          "1"
       ]
    ],
    "cube":"test_kylin_cube_with_slr_desc",
    "affectedRowCount":0,
    "isException":false,
    "exceptionMessage":null,
    "duration":3451,
    "partial":false
 }
 
```

## 用法
本框架用法和feign以及mybatis接口注解相似，能快速上手。比如：<br>
```
@KylinRepository
 public interface KylinUseDemo {
 	@KylinMethod(sql = "sql", totalSql = "totalSql")
 	List<KylinResponseTestDTO> getSkillSummaryRequestDemo(KylinPageRequestDTO<KylinRequestTestDTO> kylinPageRequestDTO);
 }
 ```
 
