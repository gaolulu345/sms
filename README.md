# tp_admin

#### 项目介绍
公司内部系统。

#### 软件架构
软件架构说明

#### 路由

- 首页  url: /index
- 登录  url:/login
- 用户  url:/pages/user
- 订单  url:/pages/order
- 退款  url:/pages/refund
- 上传 url:/pages/upload
- 系统 url:/pages/system
- 角色 url:/pages/roles
- 权限 url:/pages/permission


#### 安装教程

1. xxxx
2. xxxx
3. xxxx

#### 使用说明

1. xxxx
2. xxxx
3. xxxx

#### 数据库约束

```mysql
# 账号登陆日志普通索引
ALTER TABLE admin_account_login_log ADD INDEX index_name ( `username` ) 

```
#### 接口说明

##### 发送模板消息-会员认证审核通知
> post: /api/open/template/info/send

> 入参类型 json
```json
{
	"touser":  #模板消息接收人 *必填
	"formId":  #formId     *必填
	"page":    #要跳转的页面 *选填
	"templateInfo":  #要发送的模板的备注信息 *必填
	"data":{
		"keyword1":{
			"value":   #模板消息关键字对应的值1 姓名 *必填
		},
		"keyword2":{
		    "value": #模板消息关键字对应的值2 类型  *必填
		},
		"keyword3":{
			"value": #模板消息关键字对应的值3  状态  *必填
		},
		"keyword4":{
	    	"value": #模板消息关键字对应的值4  申请时间  *必填
		},
		"keyword5":{
			"value":#模板消息关键字对应的值5  处理时间  *必填
		}
	}
}
```
> 请求示例：
```json
{
	"touser":"oDmyu4j1SwZeJkVGehkPg81rAOlc",
	"formId":"12345678",
	"page":"pages/index",
	"templateInfo":"会员卡审核",
	"data":{
		"keyword1":{
			"value":"北北"
		},
		"keyword2":{
			"value":"会员认证"
		},
		"keyword3":{
			"value":"审核通过"
		},
		"keyword4":{
			"value":"2019年01月05日 12:30"
		},
		"keyword5":{
			"value":"2019年01月06日 12:30"
		}
	}
}
```
> 响应示例：

```json
{
    "code": "200",
    "message": "SUCCESS",
    "data": {}
}
```

##### 发送模板消息-退款通知
> post: /api/open/template/info/send
> 入参类型 json
```json
{
	"touser":  #模板消息接收人 *必填
	"formId":  #formId     *必填
	"page":    #要跳转的页面 *选填
	"templateInfo":  #要发送的模板的备注信息 *必填
	"data":{
		"keyword1":{
			"value":   #模板消息关键字对应的值1 订单编号  *必填
		},
		"keyword2":{
		    "value": #模板消息关键字对应的值2  退款金额  *必填
		},
		"keyword3":{
			"value": #模板消息关键字对应的值3 退款原因 *必填
		},
		"keyword4":{
	    	"value": #模板消息关键字对应的值4  退款时间  *必填
		}
	}
}
```
> 请求示例：
```json
{
	"touser":"o7A4G0QV_G4ZXQPGQgEPELQsmJag",
	"formId":"12345678",
	"page":"pages/index",
	"templateInfo":"微信退款通知",
	"data":{
		"keyword1":{
			"value":"NO20161128001"
		},
		"keyword2":{
			"value":"20元"
		},
		"keyword3":{
			"value":"运营商忙碌"
		},
		"keyword4":{
			"value":"2016年11月"
		}
	}
}
```
> 响应示例：
```json
{
    "code": "200",
    "message": "SUCCESS",
    "data": {}
}
```



