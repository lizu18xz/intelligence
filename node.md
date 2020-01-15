
```text
门店推荐:


百度地图获取经纬度:
 getFloat = function (number, n) {
            n = n ? parseInt(n) : 0;
            if (n <= 0) return Math.round(number);
            number = Math.round(number * Math.pow(10, n)) / Math.pow(10, n);
            return number;
        };

        $("#analyze").on("click",function(){
            $.ajax({
                type : "POST",
                url : "http://api.map.baidu.com/geocoder/v2/",
                data:"address="+encodeURIComponent($("#address").val())+"&output=json&ak=9wV8VNAp1qm8FLQzVZ70dcIS6HNwTXBk",
                dataType:"jsonp",
                jsonp:"callback",
                jsonpCallback:"showLocation",
                success : function(data){
                    if(data.status == 0){
                        alert("地址解析成功");
                        $("#latitude").val(getFloat(data.result.location.lat,6));
                        $("#longitude").val(getFloat(data.result.location.lng,6));
                    }else{
                        alert("获取百度地图失败，原因为"+data);
                    }
                },
                error : function(data){
                    alert("获取百度地图失败，原因为"+data.responseText);
                }
            });
            return false;
        });

    });

//获取当前位置的经纬度
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=FqB15cbgf1pzWzum5ajGpTrcfr7b3b90"></script>
 var geolocation = new BMap.Geolocation();
        geolocation.getCurrentPosition(function(r){
            if(this.getStatus() == BMAP_STATUS_SUCCESS){
                var longitude = r.point.lng;
                var latitude = r.point.lat;
                $("#longitude").val(longitude);
                $("#latitude").val(latitude);
                $.ajax({
                    type : "GET",
                    url : "/shop/recommend?longitude="+longitude+"&latitude="+latitude,
                    success : function(data){
                        if(data.status == 'success'){
                        }else{
                            alert("获取门店信息失败，原因为"+data.data.errMsg);
                        }
                    },
                    error : function(data){
                        alert("获取门店信息失败，原因为"+data.responseText);
                    }
                });
            }
            else {
                alert('failed'+this.getStatus());
            }
        },{enableHighAccuracy: true});

搜索:


```

### 原理
```text
搜索关键字   -->  分词 -->  分词器产生多个token ---> 通过token去搜索

搜索是以词为单位做最基本的搜索单元

依靠分词器构建分词

用分词构建倒排索引


TF/IDF打分,文档优先被搜索出来

TF:词频
这个doc文档包含了多少个这个词,包含越多表名越相关

DF:文档频率  包含该词的文档总数目

IDF:DF取反


Health  yellow
默认创建一个主分片,一个从分片
Primaries
Replicas

分片
主从
路由

```


### ES
```text

wget https://artifacts.elastic.co/downloads/kibana/kibana-7.3.0-linux-x86_64.tar.gz
https://artifacts.elastic.co/downloads/kibana/kibana-7.3.0-linux-x86_64.tar.gz

groupadd elasticsearch
useradd elasticsearch -g elasticsearch
chown -R elasticsearch:elasticsearch elasticsearch-6.4.3

su elasticsearch 启动:sh ./bin/elasticsearch   后台启动：sh ./bin/elasticsearch -d

server.port: 5601
server.host: "192.168.76.134"
server.name: "elasticsearch"
elasticsearch.url: "http://192.168.76.134:9200"
    
nohup ./kibana &   


启动的问题:(max file descriptors [4096] for elasticsearch process is too low......)
vi /etc/security/limits.conf    新增
* soft nofile 65536
* hard nofile 131072
* soft nproc 2048
* hard nproc 4096
修改配置文件sysctl.conf
vi /etc/sysctl.conf
添加下面配置：
vm.max_map_count=655360
并执行命令：
sysctl -p

访问 http://192.168.76.134:9200/


集群:
cluster.name : inte-app
node.name: node-1  （node-2,node-3）

network.host: 127.0.0.1
http.port=9200 （9201,9202）
transport.tcp.port: 9300  (9301,9302)

http.cors.enabled: true
http.cors.allow-origin: "*"
discovery.seed_hosts:["127.0.0.1:9300","127.0.0.1:9301","127.0.0.1:9302"]
cluster.initial_master_nodes: ["127.0.0.1:9300","127.0.0.1:9301","127.0.0.1:9302"]

```

### 语法
```text


/*创建索引*/

DELETE /test

PUT /test
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0
  }
}

/*非结构化创建索引*/
DELETE /employee
PUT /employee
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0
  }
}


PUT /employee/_doc/1
{
  "name":"凯杰",
  "age":12
}

/*获取索引记录*/
GET /employee/_doc/1

/*指定字段修改*/
POST /employee/_update/1
{
  "doc":{
    "name":"凯杰2"
  }
}

/*查询全部文档*/
GET /employee/_search


/*结构化创建索引*/
DELETE /employee
PUT /employee
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0
  },
  "mappings": {
    "properties": {
      "name":{
        "type": "text"
      },
      "age":{
        "type": "integer"
      }
    }
  }
}


PUT /employee/_doc/1
{
  "name":"凯杰",
  "age":12
}


PUT /employee/_create/2
{
  "name":"兄弟",
  "age":12
}

PUT /employee/_create/3
{
  "name":"兄长",
  "age":30
}

PUT /employee/_create/4
{
  "name":"兄长2",
  "age":30
}

/*不带条件查询所有记录*/
GET /employee/_search
{
  "query": {
    "match_all": {}
  }
}



/*分页查询*/
GET /employee/_search
{
  "query": {
    "match_all": {}
  },
  "from": 0,
  "size": 1
}


/*带关键字条件的查询*/
GET /employee/_search
{
  "query": {
    "match": {
      "name": "兄长"
    }
  }
}


/*带排序*/
GET /employee/_search
{
  "query": {
    "match": {
      "name": "兄"
    }
  },
  "sort": [
    {
      "age": {
        "order": "desc"
      }
    }
  ]
}

/*带filter的*/ 

GET /employee/_search
{
  "query": {
    "bool": {
      "filter": [
        {"term":{
          "name":"兄"
        }}
        ]
    }
  }

}


/*带聚合*/ 

GET /employee/_search
{
  "query": {
    "match": {
      "name": "兄"
    }
  },
  "sort": [
    {
      "age": {
        "order": "desc"
      }
    }
  ],
  "aggs": {
    "group_by_age": {
      "terms": {
        "field": "age"
      }
    }
  }
}

```

### score打分
```text
TF： token frequency   比如basketball这样的一个分词在doc字段中出现的次数
IDF：inverse doc frequency,逆文档评率，代表basketball这样的一个分词
在整个所有文档中出现的频率，取反

比如:steve jobs
十个文档,都包含steve,但是jobs只在一个里面存在,因此这个文档分数最高

TFNORM:token frequency nomalized 词频归一化,单个文档长度的考虑
比如:steve
steve jobs  分数高
steve basketball jobs 

GET /movie/_search
{
  "explain": true, 
  "query": {
    "multi_match": {
      "query": "basketball with cartoom aliens",
      "fields": ["title","overview"]
    }
  }
}


"_explanation" : {
          "value" : 8.579647,
          "description" : "max of:"   
           (默认将对应的分词在不同的字段上做TD,IDF的分数计算,将不同字段最大的分数作为这次查询的分数)
```

### 查全率和查准率
```text

查全率：正确的结果有n个,查询出来正确的有m  m/n
查准率: 查出的n个文档有m个正确 m/n

两者不可兼得,但可以调整顺序

调整score

#functionscore
#对应调整字段popularity
GET /movie/_search
{
  "query": {
    "function_score": {
      "query": {
        "multi_match": {
          "query": "steve job",
          "fields": ["title","overview"],
          "type": "most_fields"
     }
    },
     "functions": [
        {
          "field_value_factor": {
            "field": "popularity",
            "modifier": "log2p",
            "factor": 10
          }
        }
      ],
      "score_mode": "multiply"
    }
  }
}
```




### tmdb
```text
/*tmdb*/

PUT /movie
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0
  },
  "mappings": {
    "properties": {
      "title":{
        "type": "text",
        "analyzer": "english"
      },
      "tagline":{
        "type": "text",
        "analyzer": "english"
      },
      "release_date":{
        "type":"date",
        "format":"8yyyy/MM/dd||yyyy/M/dd||yyyy/MM/d||yyyy/M/d"
      },
      "popularity":{
        "type":"double"
      },
      "overview":{
        "type": "text",
        "analyzer": "english"
      },
      "cast":{
        "type": "object",
        "properties":{
          "character": {
            "type": "text",
            "analyzer": "standard"
          },
          "name": {
            "type": "text",
            "analyzer": "standard"
          }
        }
      }
    }
  }
}


//搜索内容 match 按照字段上的定义的分词分析后去索引内查询
GET /movie/_search
{
  "query": {
    "match": {
      "title": "steve zissou"
    }
  }
}


GET /movie/_analyze
{
  "field": "title",
  "text": "steve zissou"
}


//搜索内容 term 搜索引擎不会分词分析,某些意义是精确匹配

GET /movie/_search
{
  "query": {
    "term": {
      "title": "steve zissou"
    }
  }
}

//分词后的and和or的逻辑,match默认使用的是or

GET /movie/_search
{
  "query": {
    "match": {
      "title": "basketball with cartoom aliens"
    }
  }
}


GET /movie/_search
{
  "query": {
    "match": {
      "title": {
        "query": "basketball with cartoom aliens",
        "operator": "and"
      }
    }
  }
}

#最小词匹配,指定分词内容至少匹配多少次,minimum_should_match默认是1
GET /movie/_search
{
  "query": {
    "match": {
      "title": {
        "query": "basketball with cartoom aliens",
        "operator": "or",
        "minimum_should_match": 2
      }
    }
  }
}

#短语查询
GET /movie/_search
{
  "query": {
    "match_phrase": {
      "title": "steve zissou"
    }
  }
}

#多字段查询
GET /movie/_search
{
  "query": {
    "multi_match": {
      "query": "basketball with cartoom aliens",
      "fields": ["title","overview"]
    }
  }
}


# explain 打分计算的情况
GET /movie/_search
{
  "explain": true, 
  "query": {
    "match": {
      "title": "steve zissou"
    }
  }
}


#多字段查询
GET /movie/_search
{
  "query": {
    "multi_match": {
      "query": "basketball with cartoom aliens",
      "fields": ["title","overview"]
    }
  }
}

GET /movie/_search
{
  "explain": true, 
  "query": {
    "multi_match": {
      "query": "basketball with cartoom aliens",
      "fields": ["title","overview"]
    }
  }
}


# explain 打分计算的情况
GET /movie/_search
{
  "explain": true, 
  "query": {
    "match": {
      "title": "steve zissou"
    }
  }
}


# 优化多字段查询

GET /movie/_search
{
  "query": {
    "multi_match": {
      "query": "basketball with cartoom aliens",
      "fields": ["title^10","overview"]
    }
  }
}

# tie_breaker  最大命中值综合考虑其他字段命中的情况
GET /movie/_search
{
  "query": {
    "multi_match": {
      "query": "basketball with cartoom aliens",
      "fields": ["title^10","overview"],
      "tie_breaker": 0.3
    }
  }
}

#bool查询
#must:必须都为true
#must not:必须都是false
#should:其中只有一个true即可,为true的越多得分越高

GET /movie/_search
{
  "explain": true, 
  "query": {
    "bool": {
      "should": [
        {"match": {
          "title": "basketball with cartoom aliens"
        }},
        {"match": {
          "overview": "basketball with cartoom aliens"
        }}
      ]
    }
  }
  
}


#不同的multi_query其实是有不同的type
#best_fields:默认得分方式,取得最高分数作为对应文档分数,"最匹配模式"


GET /movie/_search
{
  "explain": true, 
  "query": {
    "multi_match": {
      "query": "basketball with cartoom aliens",
      "fields": ["title","overview"],
      "type": "best_fields"
    }
  }
}

#most_fields:考虑绝大多数(所有的),文档的字段得分相加,获得我们想要的结果  should

GET /movie/_search
{
  "explain": true, 
  "query": {
    "multi_match": {
      "query": "basketball with cartoom aliens",
      "fields": ["title","overview"],
      "type": "most_fields"
    }
  }
}


GET /movie/_validate/query?explain
{
  "query": {
    "multi_match": {
      "query": "basketball with cartoom aliens",
      "fields": ["title","overview"],
      "type": "most_fields"
    }
  }
}

#cross_fields:以分词为单位计算栏位的总分,取某个词(分词的字段)在不同field的最大值,然后进行sum of相加,适用于词导向的模式

GET /movie/_validate/query?explain
{
  "query": {
    "multi_match": {
      "query": "basketball with cartoom aliens",
      "fields": ["title","overview"],
      "type": "cross_fields"
    }
  }
}

#query string
#方面利用and or not
GET /movie/_search
{
  "query": {
    "query_string": {
      "fields": ["title"],
      "query": "steve AND jobs"
    }
  }
}

#filter过滤查询
GET /movie/_search
{
  "query": {
    "bool": {
      "filter": {
        "term": {
          "title": "steve"
        }
      }
    }
  }
}

#多条件过滤
GET /movie/_search
{
  "query": {
    "bool": {
      "filter": [
        {"term":{"title":"steve"}},
        {"term":{"cast.name":"gaspard"}},
        {"range":{"release_date":{"lte":"2015/01/01"}}}
        ]
    }
  }
}

```

### IK
```text
bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.3.0/elasticsearch-analysis-ik-7.3.0.zip

analyze:分析=分词的过程:字符过滤器->字符处理->分词过滤

english analyze:分析=分词的过程：字符过滤器(过滤特殊符号外加量词,the等等)->字符处理->分词过滤(分词转换,词干转化)

ik analyze:分析=分词的过程,
字符过滤器(过滤特殊符号外加量词,的,stopWord停用词)->字符处理(词库词典)->分词过滤(分词转换,词干转化)

中华人民共和国

词库词典:中华 人民,词库没有就单个汉子


#测试ik分词
_analyze?pretty
{
  "analyzer":"ik_smart",
  "text":"中华人民共和国国歌"
}

ik_smart：智能分词法

#最大化分词
GET _analyze?pretty
{
  "analyzer":"ik_max_word",
  "text":"中华人民共和国国歌"
}


#analyzer指定的是构建索引的时候的分词
#search_analyzer指定的是搜索关键字时候的分词


构建索引进行分词将内容进行分词
搜索的时候也会分词

经验:
analyzer和search_analyzer可以设置不一样,如果不配置search_analyzer默认和analyzer一样

ik_smart：查准率 好,查全率不好

ik_max_word:查全率高,查准率一般

#最佳实践:索引的时候使用max_word,但是查询的时候考虑使用smart_word
ik_smart可以分出来的时候,ik_max_word肯定可以分词出来


词库:
elasticsearch-7.3.0/plugins/analysis-ik/config
stopword.dic
extra_stopword.dic
main.dic
extra_main.dic



whitespace分词器:不做任何处理,仅仅以空格为单位来分割
#whitespace
GET _analyze?pretty
{
  "analyzer":"whitespace",
  "text":"中华人民共和国国歌 虾米"
}


{
  "tokens" : [
    {
      "token" : "中华人民共和国国歌",
      "start_offset" : 0,
      "end_offset" : 9,
      "type" : "word",
      "position" : 0
    },
    {
      "token" : "虾米",
      "start_offset" : 10,
      "end_offset" : 12,
      "type" : "word",
      "position" : 1
    }
  ]
}


```


### 索引定义
```text
#定义门店索引结构
PUT /shop
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0
  },
  "mappings": {
    "properties": {
      "id":{
        "type": "integer"
      },
      "name":{
        "type": "text",
        "analyzer": "ik_max_word",
        "search_analyzer": "ik_smart"
      },
      "tags":{
        "type": "text",
        "analyzer": "whitespace",
        "fielddata": true
      },
      "location":{
        "type": "geo_point"
      },
      "remark_score":{
        "type": "double"
      },
      "price_per_man":{
        "type": "integer"
      },
      "category_id":{
        "type": "integer"
      },
      "category_name":{
        "type": "keyword"
      },
      "sell_id":{
        "type": "integer"
      },
      "seller_remark_score":{
        "type": "double"
      },
      "seller_disabled_flag":{
        "type": "integer"
      }
    }
  }
}

```
### 索引构件
```text
logstash-input-jdbc
全量：
bin/logstash-plugin install  logstash-input-jdbc

input {
    jdbc {
      #设置timezone
      jdbc_default_timezone => "Asia/Shanghai"
      # mysql 数据库链接,dianpingdb为数据库名
      jdbc_connection_string => "jdbc:mysql://127.0.0.1:3306/dianpingdb"
      # 用户名和密码
      jdbc_user => "root"
      jdbc_password => "root123"
      # 驱动
      jdbc_driver_library => "/logstash-7.3.0/bin/mysql/mysql-connector-java-5.1.34.jar"
      # 驱动类名
      jdbc_driver_class => "com.mysql.jdbc.Driver"
      jdbc_paging_enabled => "true"
      jdbc_page_size => "50000"
      last_run_metadata_path => "/logstash-7.3.0/bin/mysql/last_value_meta" 
    # 执行的sql 文件路径+名称;
      statement_filepath => "/logstash-7.3.0/bin/mysql/jdbc.sql"
      # 设置监听间隔  各字段含义（由左至右）分、时、天、月、年，全部为*默认含义为每分钟都更新
      schedule => "* * * * *"
    }
}

output {
    elasticsearch {
      # ES的IP地址及端口
        hosts => ["localhost:9200"]
      # 索引名称
        index => "shop"
  	document_type => "_doc"
      # 自增ID 需要关联的数据库中有有一个id字段，对应索引的id号
        document_id => "%{id}"
    }
    stdout {
     # JSON格式输出
        codec => json_lines
    }
}

select a.id,a.name,a.tags,concat(a.latitude,',',a.longitude) as location,a.remark_score,
a.price_per_man,a.category_id,b.name as category_name,a.seller_id,c.remark_score as seller_remark_score,
c.disabled_flag as seller_disabled_flag from shop a inner join category b on a.category_id = b.id inner join seller c 
on c.id = a.seller_id where a.updated_at > :sql_last_value or b.updated_at > :sql_last_value or c.updated_at > :sql_last_value

启动:
./logstash -f mysql/jdbc.conf

增量：

[2020-01-12T10:16:04,247][INFO ][logstash.inputs.jdbc     ] (0.014551s) SELECT version()
[2020-01-12T10:16:04,315][INFO ][logstash.inputs.jdbc     ] (0.002009s) SELECT version()
[2020-01-12T10:16:05,493][INFO ][logstash.inputs.jdbc     ] (0.008928s) SELECT count(*) AS `count` FROM (select a.id,a.name,a.tags,concat(a.latitude,',',a.longitude) as location,a.remark_score,a.price_per_man,a.category_id,b.name as category_name,a.seller_id,c.remark_score as seller_remark_score,c.disabled_flag as seller_disabled_flag from shop a inner join category b on a.category_id = b.id inner join seller c on c.id = a.seller_id where a.updated_at > '2020-01-02 06:12:56' or b.updated_at > '2020-01-02 06:12:56' or c.updated_at > '2020-01-02 06:12:56'
) AS `t1` LIMIT 1
[2020-01-12T10:16:05,551][INFO ][logstash.inputs.jdbc     ] (0.001950s) SELECT * FROM (select a.id,a.name,a.tags,concat(a.latitude,',',a.longitude) as location,a.remark_score,a.price_per_man,a.category_id,b.name as category_name,a.seller_id,c.remark_score as seller_remark_score,c.disabled_flag as seller_disabled_flag from shop a inner join category b on a.category_id = b.id inner join seller c on c.id = a.seller_id where a.updated_at > '2020-01-02 06:12:56' or b.updated_at > '2020-01-02 06:12:56' or c.updated_at > '2020-01-02 06:12:56'
) AS `t1` LIMIT 50000 OFFSET 0


```

### 搜索接入
```text
基本搜索测试



搜索模型建立




#带上距离字段
#params 入参
#haversin 系统函数


#带上距离字段
GET /shop/_search
{
  "query": {
    "match": {
      "name": "凯悦"
    }
  },
  "_source": "*", 
  "script_fields": {
    "distance": {
      "script":{
        "source": "haversin(lat,lon,doc['location'].lat,doc['location'].lon)",
        "lang":"expression",
        "params": {"lat":31.37,"lon":127.12}
      } 
    }
  }
}

距离单位:公里

#使用距离排序
GET /shop/_search
{
  "query": {
    "match": {
      "name": "凯悦"
    }
  },
  "_source": "*", 
  "script_fields": {
    "distance": {
      "script":{
        "source": "haversin(lat,lon,doc['location'].lat,doc['location'].lon)",
        "lang":"expression",
        "params": {"lat":31.37,"lon":127.12}
      } 
    }
  },
  "sort": [
    {
      "_geo_distance": {
        "location": {
          "lat": 31.37,
          "lon": 127.12
        },
        "order": "asc",
        "unit": "km",
        "distance_type": "arc"
      }
    }
  ]
}


#使用function score解决排序模型
GET /shop/_search
{
  "_source": "*",
  "script_fields": {
    "distance": {
      "script":{
        "source": "haversin(lat,lon,doc['location'].lat,doc['location'].lon)",
        "lang":"expression",
        "params": {"lat":31.306172,"lon":121.525843}
      } 
    }
  },
  "query": {
    "function_score": {
      "query": {
        "bool": {
          "must": [
            {
              "match": {
                "name": {"query": "凯悦"}
              }
            },
            {
              "term": {
              "seller_disabled_flag": 0
              }
            }
          ]
        }
      },
      "functions": [
        {
          "gauss": {
            "location": {
              "origin": "31.306172,121.525843",
              "scale": "100km",
              "offset": "0km",
              "decay": 0.5
            }
          }
        }
      ]
    }
  }
}

#距离主导地位,兼顾门店的打分和卖家的评价
GET /shop/_search
{
  "_source": "*",
  "script_fields": {
    "distance": {
      "script":{
        "source": "haversin(lat,lon,doc['location'].lat,doc['location'].lon)",
        "lang":"expression",
        "params": {"lat":31.306172,"lon":121.525843}
      } 
    }
  },
  "query": {
    "function_score": {
      "query": {
        "bool": {
          "must": [
            {
              "match": {
                "name": {"query": "凯悦","boost": 0.5}
              }
            },
            {
              "term": {
              "seller_disabled_flag": 0
              }
            }
          ]
        }
      },
      "functions": [
        {
          "gauss": {
            "location": {
              "origin": "31.306172,121.525843",
              "scale": "100km",
              "offset": "0km",
              "decay": 0.5
            }
          },
          "weight": 9
        },
        {
          "field_value_factor": {
            "field": "remark_score"
          },
          "weight": 0.2
        },
        {
          "field_value_factor": {
            "field": "seller_remark_score"
          },
          "weight": 0.1
        }
      ],
      "score_mode": "sum",
      "boost_mode": "sum"
    }
  }, 
  "sort": [
    {
      "_score": {
        "order": "desc"
      }
    }
  ]
}
```

### java接入
```text
Node 接入
transport 接入  9300

官方推荐:
rest api 接入   http请求  9200
HIGH LEVEL

        <dependency>
            <groupId>org.elasticsearch.client</groupId>
            <artifactId>elasticsearch-rest-client</artifactId>
            <version>7.3.0</version>
        </dependency>

        <dependency>
            <groupId>org.elasticsearch</groupId>
            <artifactId>elasticsearch</artifactId>
            <version>7.3.0</version>
        </dependency>

        <dependency>
            <groupId>org.elasticsearch.client</groupId>
            <artifactId>elasticsearch-rest-high-level-client</artifactId>
            <version>7.3.0</version>
        </dependency>

```