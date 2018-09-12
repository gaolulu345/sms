
var menuPer, opPer;

function compare(property){
    return function(obj1,obj2){
         var value1 = obj1[property];
         var value2 = obj2[property];
         return value1 - value2;     // 升序
    }
}

// 获取用户权限
// $.ajax({
// 	url: '/api/private/partner/permission',
//     dataType : 'json',
//     type : "post",
//     data: {},
//     async : false,
//     contentType : "application/json;charset=UTF-8",
// 	success: function(res) {
// 		if(res.code == 200) {
// 			opPer = res.data.op;

// 			let hashOpPer = {}
// 			opPer.forEach(function(val){
// 				hashOpPer[val.url] = val
// 			})
// 			opPer = hashOpPer
// 			// console.log(hashOpPer)
// 			let menu = res.data.menu;
// 			menu.forEach(function(val) {
// 	            val.class = val.url.substring(7)
// 	        })
// 	        menu = menu.sort(compare("order"))
// 			menuPer = menu;
// 		} else {
// 			window.location.href = '/login';
// 		}
// 	},
// 	fail: function() {
// 		window.location.href = '/login';
// 	}
// })

function logout(id) {
	$.ajax({
		url: '/api/user/logout?userId=' + id,
		dataType: 'json',
		type: 'post',
		async: false,
		contentType : 'application/json;charset=UTF-8',
		success: function(data){
			console.log(data);
			if(data.code == 200){
				 window.location.href = '/login';
			} else {
				vm.$message.error(data.message);
			}
		}
	})
}

function openChangePwDialog() {
	$('#changePwDialog').removeClass('none')
}
function closeChangePwDialog() {
	$('#changePwDialog').addClass('none')
}
function changePw() {
	let opw = hex_md5($('#opw').val())
	let npw = hex_md5($('#npw').val())
	let snpw = hex_md5($('#snpw').val())
	console.log(opw, npw, snpw)
	let data = {opw: opw, npw: npw, snpw: snpw}

	if( npw === snpw) {
		$.ajax({
			url: '/api/private/admin/update/pw',
			dataType: 'json',
			data: JSON.stringify(data),
			type: 'post',
			async: false,
			contentType : 'application/json;charset=UTF-8',
			success: function(data){
				console.log(data);
				if(data.code == 200){
					 window.location.href = '/login';
				} else {
					vm.$message.error(data.message);
				}
			}
		})
	} else {
		vm.$message.error('新密码两次输入不一致！');
	}
}
