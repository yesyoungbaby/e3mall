var E3MALL = {
	checkLogin : function(){
		// 从浏览器取token
		var _ticket = $.cookie("token");
		if(!_ticket){
			// 取不到直接返回
			return ;
		}
		$.ajax({
			// 取到token做ajax请求
			// 此处存在跨越问题 即浏览器不允许8080端口的服务加载8088发来的数据，实际上请求是发出去的，88也是做了响应的
			// 域名不同 端口号不同
			url : "http://localhost:8088/user/token/" + _ticket,
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var username = data.data.username;
					var html = username + "，欢迎来到宜立方购物网！<a href=\"http://www.e3mall.cn/user/logout.html\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	E3MALL.checkLogin();
});