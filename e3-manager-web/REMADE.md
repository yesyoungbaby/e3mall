# 商城的管理web系统
## 是什么？
    jd这些我们用户是无法添加商品的。
    所以正常的商城项目商品的添加是归为后台管理系统的。
# 做了什么
    e3-manager提供了商品服务，即商品的crud，对应item表。调用商品服务manager
    管理e3商城首页内容，对外提供内容服务。对应content表。调用内容服务CMS
# 关Dubbo
    看配置文件。
    dubbo中一个接口算一个服务/功能，该接口中提供了n个功能方法。
    manager的applicationContext-service.xml表明对外提供了几个服务。
    ItemCatService提供了一个方法：获取商品类目。其功能还是服务商品添加功能。
        后续看其他服务需不需要该服务，到底有没有必要单独提出来作为一个接口。
