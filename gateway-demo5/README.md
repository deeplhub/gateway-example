# 网关学习用例

实现动态路由（Nacos读取路由配置文件）

**gateway-router.json**

```json
[
    {
        "id": "csdn",
        "uri": "https://blog.csdn.net",
        "predicates": [
            {
                "args": {
                    "pattern": "/csdn"
                },
                "name": "Path"
            }
        ]
    },
        {
        "id": "cnblogs",
        "uri": "https://www.cnblogs.com",
        "predicates": [
            {
                "args": {
                    "pattern": "/cnblogs"
                },
                "name": "Path"
            }
        ]
    }
]
```